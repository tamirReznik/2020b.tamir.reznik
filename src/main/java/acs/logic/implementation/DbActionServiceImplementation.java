package acs.logic.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import acs.dal.ActionDao;
import acs.data.ActionEntity;
import acs.data.Converter;
import acs.data.UserRole;
import acs.logic.EnhancedActionService;
import acs.logic.EnhancedElementService;
import acs.logic.EnhancedUserService;
import acs.logic.ServiceTools;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.action.ActionType;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.element.ElementType;
import acs.rest.boundaries.element.Location;
import acs.rest.boundaries.user.UserBoundary;

@Service
public class DbActionServiceImplementation implements EnhancedActionService {
	private String projectName;
	private ActionDao actionDao;
	private Converter converter;
	private EnhancedElementService elementService;
	private EnhancedUserService userService;

	@Autowired
	public DbActionServiceImplementation(ActionDao actionDao, Converter converter, EnhancedUserService userService,
			EnhancedElementService elementService) {
		this.converter = converter;
		this.actionDao = actionDao;
		this.elementService = elementService;
		this.userService = userService;
	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional // (readOnly = false)
	public Object invokeAction(ActionBoundary action) {

		if (action == null || action.getType() == null)
			throw new RuntimeException("ActionBoundary received in invokeAction method can't be null\n");

		UserBoundary userBoundary = this.userService.login(action.getInvokedBy().getUserId().getDomain(),
				action.getInvokedBy().getUserId().getEmail());

		if (!userBoundary.getRole().equals(UserRole.PLAYER))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only player can invoke action");

		ElementBoundary element = this.elementService.getSpecificElement(action.getInvokedBy().getUserId().getDomain(),
				action.getInvokedBy().getUserId().getEmail(), action.getElement().getElementId().getDomain(),
				action.getElement().getElementId().getId());

		if (!element.getActive())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "element of action must be active");

		if (element.getType().equals(ElementType.car.name()))
			updateCarLocation(action, userBoundary, element);

		if (action.getType().toLowerCase().equals(ActionType.park.name())) {
			ElementBoundary parkingElement = parkOrDepart(element, userBoundary, false, action);
			saveAction(action);
			return parkingElement;
		}
		if (action.getType().toLowerCase().equals(ActionType.depart.name())) {
			ElementBoundary parkingElement = parkOrDepart(element, userBoundary, true, action);
			saveAction(action);
			return parkingElement;
		}

		if (action.getType().toLowerCase().equals(ActionType.search.name())) {
			ElementBoundary elementArr[] = search(element, userBoundary, 5, action);
			saveAction(action);
			return elementArr;
		}

		saveAction(action);
		return action;

	}

	public void updateCarLocation(ActionBoundary action, UserBoundary ue, ElementBoundary element) {
		HashMap<String, Double> location = action.getActionAttributes().containsKey("location")
				? (HashMap<String, Double>) action.getActionAttributes().get("location")
				: new HashMap<>();

		if (!location.isEmpty())
			element.setLocation(new Location(location.get("lat"), location.get("lng")));

		toManager(ue);
		elementService.update(ue.getUserId().getDomain(), ue.getUserId().getEmail(), element.getElementId().getDomain(),
				element.getElementId().getId(), element);
		toPlayer(ue);
	}

	public void saveAction(ActionBoundary action) {
		ActionIdBoundary aib = new ActionIdBoundary(projectName, UUID.randomUUID().toString());
		action.setCreatedTimestamp(new Date());
		action.setActionId(aib);
		ActionEntity entity = converter.toEntity(action);
		this.actionDao.save(entity);
	}

	public ElementBoundary[] search(ElementBoundary car, UserBoundary user, double distance, ActionBoundary action) {

//		ElementBoundary[] parkings = elementService.searchByLocationAndType(user.getUserId().getDomain(),
//				user.getUserId().getEmail(), car.getLocation().getLat(), car.getLocation().getLng(), distance,
//				ElementType.parking.name(), 36, 0).toArray(new ElementBoundary[0]);

//		return  elementService.searchByLocationAndType(user.getUserId().getDomain(), user.getUserId().getEmail(),
//				car.getLocation().getLat(), car.getLocation().getLng(), distance, ElementType.parking.name(), 36, 0)
//				.toArray(new ElementBoundary[0]);

		return Stream
				.concat(Arrays.stream(elementService.searchByLocationAndType(user.getUserId().getDomain(),
						user.getUserId().getEmail(), car.getLocation().getLat(), car.getLocation().getLng(), distance,
						ElementType.parking.name(), 36, 0).toArray(new ElementBoundary[0])),
						Arrays.stream(
								elementService
										.searchByLocationAndType(user.getUserId().getDomain(),
												user.getUserId().getEmail(), car.getLocation().getLat(),
												car.getLocation().getLng(), distance, ElementType.parking.name(), 36, 0)
										.toArray(new ElementBoundary[0])))
				.toArray(ElementBoundary[]::new);
	}

	public ElementBoundary parkOrDepart(ElementBoundary car, UserBoundary user, boolean depart, ActionBoundary action) {

		ElementBoundary parkingBoundary = null;
		double distanceFromCar = 0.0002;

		toManager(user);
		ElementBoundary[] parking = elementService
				.getAnArrayWithElementParent(user.getUserId().getDomain(), user.getUserId().getEmail(),
						car.getElementId().getDomain(), car.getElementId().getId(), 1, 0)
				.toArray(new ElementBoundary[0]);

//		check if user already parking - not allowed 
		if (parking.length > 0 && depart == false)
			if (!parking[0].getActive())
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"You cannot park when you are already parked ;<");

//		check if user need to depart specific parking
		if (parking.length > 0 && depart == true)
			if (!parking[0].getActive()) {

				parkingBoundary = updateParking(parkingBoundary, car, depart, user, parking[0]);
				toPlayer(user);
				return parkingBoundary;
			}

//Searching for nearby parking to occupy 
		ElementBoundary[] parkingNearby = this.elementService.searchByLocationAndType(user.getUserId().getDomain(),
				user.getUserId().getEmail(), car.getLocation().getLat(), car.getLocation().getLng(), distanceFromCar,
				ElementType.parking.name(), 20, 0).toArray(new ElementBoundary[0]);

		ElementBoundary[] parkingLotNearBy = this.elementService.searchByLocationAndType(user.getUserId().getDomain(),
				user.getUserId().getEmail(), car.getLocation().getLat(), car.getLocation().getLng(),
				distanceFromCar * 4, ElementType.parking_lot.name(), 20, 0).toArray(new ElementBoundary[0]);

		UserBoundary userBoundary = toManager(user);

		if (parkingLotNearBy.length > 0)
			parkingBoundary = updateParkingLot(parkingBoundary, car, depart, userBoundary, parkingLotNearBy);
		else if (parkingNearby.length > 0)
			parkingBoundary = updateParking(parkingBoundary, car, depart, userBoundary, parkingNearby);

//		if we didn't found parking nearby -> create new one
		if (parkingBoundary == null)
			parkingBoundary = createParking(car, depart, userBoundary);

//		Bind each car to parking or parking-lot

		this.elementService.bindExistingElementToAnExsitingChildElement(userBoundary.getUserId().getDomain(),
				userBoundary.getUserId().getEmail(), parkingBoundary.getElementId(),
				new ElementIdBoundary(car.getElementId().getDomain(), car.getElementId().getId()));

		toPlayer(user);

		return parkingBoundary;
	}

	public UserBoundary toPlayer(UserBoundary user) {
		user.setRole(UserRole.PLAYER);
		return userService.updateUser(user.getUserId().getDomain(), user.getUserId().getEmail(), user);
//		return converter.fromEntity(this.userDao.save(user));

	}

	public UserBoundary toManager(UserBoundary user) {
		user.setRole(UserRole.MANAGER);

		return userService.updateUser(user.getUserId().getDomain(), user.getUserId().getEmail(), user);

	}

	public ElementBoundary updateParking(ElementBoundary parkingBoundary, ElementBoundary car, boolean depart,
			UserBoundary userBoundary, ElementBoundary... parkingNearby) {
		parkingBoundary = ServiceTools.getClosest(car, parkingNearby);

		parkingBoundary.getElementAttributes().put("LastCarReport",
				new ElementIdBoundary(car.getElementId().getDomain(), car.getElementId().getId()));

		parkingBoundary.getElementAttributes().put("lastReportTimestamp", new Date());
		parkingBoundary.setActive(depart);
		this.elementService.update(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail(),
				parkingBoundary.getElementId().getDomain(), parkingBoundary.getElementId().getId(), parkingBoundary);
		return parkingBoundary;
	}

	public ElementBoundary createParking(ElementBoundary car, boolean depart, UserBoundary userBoundary) {

		HashMap<String, Object> currentParkingAttributes = new HashMap<>();
		currentParkingAttributes.put("LastCarReport",
				new ElementIdBoundary(car.getElementId().getDomain(), car.getElementId().getId()));
		currentParkingAttributes.put("lastReportTimestamp", new Date());

		ElementBoundary parkingBoundary = new ElementBoundary(new ElementIdBoundary("", ""), ElementType.parking.name(),
				"parking_name", depart, new Date(), car.getLocation(), currentParkingAttributes, car.getCreatedBy());

		return this.elementService.create(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail(),
				parkingBoundary);
	}

	public ElementBoundary updateParkingLot(ElementBoundary parkingBoundary, ElementBoundary car, boolean depart,
			UserBoundary userBoundary, ElementBoundary... parkingLotNearBy) {

		parkingBoundary = ServiceTools.getClosest(car, parkingLotNearBy);

		List<ElementIdBoundary> carList = null;
		int counter = 0;

		if (parkingBoundary.getElementAttributes().containsKey("carList")) {
			carList = (ArrayList<ElementIdBoundary>) parkingBoundary.getElementAttributes().get("carList");
		} else
			carList = new ArrayList<>();

		if (!parkingBoundary.getElementAttributes().containsKey("capacity")) {
			parkingBoundary.getElementAttributes().put("capacity", 80);
		}
		parkingBoundary.getElementAttributes().put("capacity", 1);

		if (parkingBoundary.getElementAttributes().containsKey("carCounter")) {
			counter = (int) parkingBoundary.getElementAttributes().get("carCounter");
			parkingBoundary.getElementAttributes().put("carCounter", counter + 1);
		} else
			parkingBoundary.getElementAttributes().put("carCounter", 1);

		carList.add(new ElementIdBoundary(car.getElementId().getDomain(), car.getElementId().getId()));
		parkingBoundary.getElementAttributes().put("carList", carList);
		parkingBoundary.getElementAttributes().put("capacity", counter + 1);
		parkingBoundary.getElementAttributes().put("lastReportTimestamp", new Date());
		parkingBoundary.setActive(depart);
		this.elementService.update(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail(),
				parkingBoundary.getElementId().getDomain(), parkingBoundary.getElementId().getId(), parkingBoundary);

		return parkingBoundary;

	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		Iterable<ActionEntity> allActions = this.actionDao.findAll();

		List<ActionBoundary> rv = new ArrayList<>();
		for (ActionEntity ent : allActions)
			rv.add(this.converter.fromEntity(ent));

		return rv;

	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllActions(String adminDomain, String adminEmail) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		UserBoundary uE = this.userService.login(adminDomain, adminEmail);

		if (!uE.getRole().equals(UserRole.ADMIN))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only admin can delete all actions");

		this.actionDao.deleteAll();

	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		UserBoundary uE = this.userService.login(adminDomain, adminEmail);

		if (!uE.getRole().equals(UserRole.ADMIN))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only admin can get all actions");

		ServiceTools.validatePaging(size, page);

		return this.actionDao.findAll(PageRequest.of(page, size, Direction.DESC, "actionId"))// Page<ActionEntity>
				.getContent()// List<ActionEntity>
				.stream()// Stream<ActionEntity>
				.map(this.converter::fromEntity)// Stream<ActionEntity>
				.collect(Collectors.toList()); // List<ActionEntity>

	}
}
