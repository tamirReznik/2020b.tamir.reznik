package acs.logic.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.Converter;
import acs.data.ElementEntity;
import acs.data.ElementIdEntity;
import acs.data.UserEntity;
import acs.data.UserIdEntity;
import acs.data.UserRoleEntityEnum;
import acs.logic.ElementService;
import acs.logic.EnhancedActionService;
import acs.logic.EnhancedElementService;
import acs.logic.EnhancedUserService;
import acs.logic.ObjectNotFoundException;
import acs.logic.ServiceTools;
import acs.logic.UserService;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.user.UserBoundary;

@Service
public class DbActionServiceImplementation implements EnhancedActionService {
	private String projectName;
	private ActionDao actionDao;
	private Converter converter;
	private ElementDao elementDao;
	private UserDao userDao;
	private EnhancedUserService userService;
	private EnhancedElementService elementService;

	@Autowired
	public DbActionServiceImplementation(ActionDao actionDao, ElementDao elementDao, UserDao userDao,
			Converter converter, EnhancedUserService userService, EnhancedElementService elementService) {
		this.converter = converter;
		this.actionDao = actionDao;
		this.elementDao = elementDao;
		this.userDao = userDao;
		this.userService = userService;
		this.elementService = elementService;
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

		UserEntity ue = this.userDao.findById(this.converter.toEntity(action.getInvokedBy().getUserId()))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find object by ElementDomain:" + action.getInvokedBy().getUserId().getDomain()
								+ " or ElementId:" + action.getInvokedBy().getUserId().getEmail()));

		if (!ue.getRole().equals(UserRoleEntityEnum.player))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only player can invoke action");

		ElementIdEntity elementIdOfAction = this.converter.fromElementIdBoundary(action.getElement().getElementId());

		ElementEntity element = this.elementDao.findById(elementIdOfAction)
				.orElseThrow(() -> new ObjectNotFoundException("could not find object by ElementDomain:"
						+ elementIdOfAction.getElementDomain() + " or ElementId:" + elementIdOfAction.getId()));

		if (!element.getActive())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "element of action must be active");

		if (action.getType().toLowerCase().equals("park"))
			parkOrDepart(element, ue, true);
		if (action.getType().toLowerCase().equals("depart"))
			parkOrDepart(element, ue, false);
		if (action.getType().toLowerCase().equals("search"))
			return search(element, ue);

		ActionIdBoundary aib = new ActionIdBoundary(projectName, UUID.randomUUID().toString());
		action.setCreatedTimestamp(new Date());
		action.setActionId(aib);
		ActionEntity entity = converter.toEntity(action);
		this.actionDao.save(entity);
		return action;

	}

	public ElementBoundary[] search(ElementEntity car, UserEntity user) {

		return elementService
				.searchByLocationAndType(user.getUserId().getDomain(), user.getUserId().getEmail(),
						car.getLocation().getLat(), car.getLocation().getLng(), 1500, "parking", 36, 0)
				.toArray(new ElementBoundary[0]);

	}

	public void parkOrDepart(ElementEntity car, UserEntity user, boolean depart) {

		ElementBoundary parkingBoundary = null;
		ElementBoundary postedParking = null;
		double distanceFromCar = 2;

//Searching for nearby parking to occupy 
		ElementBoundary[] parkingNearBy = this.elementService
				.searchByLocationAndType(user.getUserId().getDomain(), user.getUserId().getEmail(),
						car.getLocation().getLat(), car.getLocation().getLng(), distanceFromCar, "parking", 16, 0)
				.toArray(new ElementBoundary[0]);

//		create a manager so we can update and creates elements
		user.setRole(UserRoleEntityEnum.manager);

		UserBoundary userBoundary = this.userService.updateUser(user.getUserId().getDomain(),
				user.getUserId().getEmail(), this.converter.fromEntity(user));

//		update attribute - date of last report + element boundary of last
		HashMap<String, Object> currentParkingAttributes = new HashMap<>();
		currentParkingAttributes.put("LastCarReport",
				new ElementIdBoundary(car.getElementId().getElementDomain(), car.getElementId().getId()));
		currentParkingAttributes.put("lastReportTimestamp", new Date());

//		if we didn't found parking nearby -> create new one
		if (parkingNearBy.length == 0) {

			parkingBoundary = new ElementBoundary(new ElementIdBoundary(), "parking", car.getElementId().getId(),
					depart, new Date(), car.getLocation(), currentParkingAttributes, car.getCreateBy());

			postedParking = this.elementService.create(user.getUserId().getDomain(), user.getUserId().getEmail(),
					parkingBoundary);
//		If we found parking nearby - we update it 
		} else {
			double minDistance = 100, tempDistance;
			for (ElementBoundary elementBoundary : parkingNearBy) {
				tempDistance = ServiceTools.distance(car.getLocation().getLat(), car.getLocation().getLng(),
						elementBoundary.getLocation().getLat(), elementBoundary.getLocation().getLng());
				if (tempDistance < minDistance) {
					minDistance = tempDistance;
					parkingBoundary = elementBoundary;
				}

			}

			if (parkingBoundary.getType().equals("parking_lot"))
				parkingBoundary.getElementAttributes().put("occupancy",
						(Integer) parkingBoundary.getElementAttributes().get("occupancy") + 1);

			parkingBoundary.setActive(depart);
			parkingBoundary.setElementAttributes(currentParkingAttributes);
			this.elementService.update(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail(),
					parkingBoundary.getElementId().getDomain(), parkingBoundary.getElementId().getId(),
					parkingBoundary);

		}

//		Bind each car to single parking
//		this.elementService.bindExistingElementToAnExsitingChildElement(userBoundary.getUserId().getDomain(),
//				user.getUserId().getEmail(), postedParking.getElementId(),
//				new ElementIdBoundary(car.getElementId().getElementDomain(), car.getElementId().getId()));
//
		user.setRole(UserRoleEntityEnum.player);

		userBoundary = this.userService.updateUser(user.getUserId().getDomain(), user.getUserId().getEmail(),
				this.converter.fromEntity(user));

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

		UserEntity uE = this.userDao.findById(new UserIdEntity(adminDomain, adminEmail))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find user by userDomain: " + adminDomain + "and userEmail: " + adminEmail));

		if (!uE.getRole().equals(UserRoleEntityEnum.admin))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only admin can delete all actions");

		this.actionDao.deleteAll();

	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		UserEntity uE = this.userDao.findById(new UserIdEntity(adminDomain, adminEmail))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find user by userDomain: " + adminDomain + "and userEmail: " + adminEmail));

		if (!uE.getRole().equals(UserRoleEntityEnum.admin))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "only admin can get all actions");

		ServiceTools.validatePaging(size, page);

		return this.actionDao.findAll(PageRequest.of(page, size, Direction.DESC, "actionId"))// Page<ActionEntity>
				.getContent()// List<ActionEntity>
				.stream()// Stream<ActionEntity>
				.map(this.converter::fromEntity)// Stream<ActionEntity>
				.collect(Collectors.toList()); // List<ActionEntity>

	}
}
