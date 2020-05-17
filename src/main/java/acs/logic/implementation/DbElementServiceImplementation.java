package acs.logic.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.Converter;
import acs.data.ElementEntity;
import acs.data.ElementIdEntity;
import acs.data.UserEntity;
import acs.data.UserIdEntity;
import acs.data.UserRoleEntityEnum;
import acs.logic.EnhancedElementService;
import acs.logic.ObjectNotFoundException;
import acs.logic.ServiceTools;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.user.UserBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

@Service
public class DbElementServiceImplementation implements EnhancedElementService {
	private ElementDao elementDao;
	private Converter converter;
	private String projectName;
	private UserDao userDao;

	@Autowired
	public DbElementServiceImplementation(ElementDao elementDao, Converter converter, UserDao userDao) {
		this.elementDao = elementDao;
		this.converter = converter;
		this.userDao = userDao;
	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary elementDetails) {

		ServiceTools.stringValidation(managerDomain, managerEmail);

		UserIdEntity uib = new UserIdEntity(managerDomain, managerEmail);
		UserEntity existing = this.userDao.findById(uib).orElseThrow(() -> new ObjectNotFoundException(
				"could not find object by UserDomain:" + managerDomain + "or userEmail:" + managerEmail));

		if (!existing.getRole().equals(UserRoleEntityEnum.manager))
			throw new ObjectNotFoundException("You are not manager! Can't create an element");

		elementDetails.setElementId(new ElementIdBoundary(projectName, UUID.randomUUID().toString()));
		ElementEntity entity = this.converter.toEntity(elementDetails);
		entity.setTimeStamp(new Date());
		Map<String, UserIdBoundary> createdBy = new HashMap<>();
		createdBy.put("userId", new UserIdBoundary(managerDomain, managerEmail));
		entity.setCreateBy(createdBy);
		return this.converter.fromEntity(this.elementDao.save(entity));

	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		ServiceTools.stringValidation(managerDomain, managerEmail, elementDomain, elementId);

		UserIdEntity uib = new UserIdEntity(managerDomain, managerEmail);

		UserEntity existingUser = this.userDao.findById(uib).orElseThrow(() -> new ObjectNotFoundException(
				"could not find object by UserDomain:" + managerDomain + "or userEmail:" + managerEmail));

		if (!existingUser.getRole().equals(UserRoleEntityEnum.manager))
			throw new ObjectNotFoundException("You are not manager! Can't update an element");

		ElementIdEntity elementIdEntity = new ElementIdEntity(elementDomain, elementId);
		ElementEntity existing = this.elementDao.findById(elementIdEntity)
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find object by elementDomain: " + elementDomain + "or elementId: " + elementId));
//TODO IF we want to update to null ?
		if (update.getActive() != null)
			existing.setActive(update.getActive());

		if (update.getName() != null)
			existing.setName(update.getName());

		if (update.getLocation() != null)
			existing.setLocation(update.getLocation());

		if (update.getType() != null)
			existing.setType(update.getType());

		if (update.getElementAttributes() != null)
			existing.setElemntAttributes(update.getElementAttributes());

		return this.converter.fromEntity(this.elementDao.save(existing));

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		ServiceTools.stringValidation(userDomain, userEmail);

		Iterable<ElementEntity> allElements = this.elementDao.findAll();
		List<ElementBoundary> returnElements = new ArrayList<>();

		for (ElementEntity entity : allElements) {
			returnElements.add(this.converter.fromEntity(entity)); // map entities to boundaries
		}

		return returnElements;

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page) {

		ServiceTools.stringValidation(userDomain, userEmail);

		ServiceTools.validatePaging(size, page);

		UserIdEntity uib = new UserIdEntity(userDomain, userEmail);
		UserEntity existingUser = this.userDao.findById(uib).orElseThrow(() -> new ObjectNotFoundException(
				"could not find object by UserDomain:" + userDomain + "or userEmail:" + userEmail));

		// if user is MANAGER : findAll
		if (existingUser.getRole().equals(UserRoleEntityEnum.manager)) {
			return this.elementDao.findAll(PageRequest.of(page, size, Direction.DESC, "name")) // Page<ElementEntity>
					.getContent() // List<ElementEntity>
					.stream() // Stream<ElementEntity>
					.map(this.converter::fromEntity) // Stream<ElementBoundary>
					.collect(Collectors.toList()); // List<ElementBoundary>
		}

		// if user = PLAYER : findAllByActive
		else if (existingUser.getRole().equals(UserRoleEntityEnum.player)) {
			return this.elementDao.findAllByActive(Boolean.TRUE, PageRequest.of(page, size, Direction.DESC, "name")) // Page<ElementEntity>
					.stream() // Stream<ElementEntity>
					.map(this.converter::fromEntity) // Stream<ElementBoundary>
					.collect(Collectors.toList()); // List<ElementBoundary>
		}

		return new ArrayList<>();
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {

		ServiceTools.stringValidation(userDomain, userEmail, elementDomain, elementId);

		UserEntity uE = this.userDao.findById(new UserIdEntity(userDomain, userEmail))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find user by userDomain: " + userDomain + "and userEmail: " + userEmail));
		if (uE.getRole() == UserRoleEntityEnum.admin)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin User Can't Get Specific Element");

		ElementEntity existing = this.elementDao.findById(new ElementIdEntity(elementDomain, elementId))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find object by elementDomain: " + elementDomain + "or elementId: " + elementId));

		if (uE.getRole() == UserRoleEntityEnum.player && !existing.getActive())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player User Can't Get Specific Inactive Element");

		return this.converter.fromEntity(existing);

	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		ServiceTools.stringValidation(adminDomain, adminEmail);
		this.elementDao.deleteAll();
	}

	@Override
	@Transactional
	public void bindExistingElementToAnExsitingChildElement(ElementIdBoundary originId, ElementIdBoundary responseId) {

		ElementEntity origin = this.elementDao.findById(converter.fromElementIdBoundary(originId))
				.orElseThrow(() -> new ObjectNotFoundException("could not find origin by id:" + originId));

		ElementEntity response = this.elementDao.findById(converter.fromElementIdBoundary(responseId))
				.orElseThrow(() -> new ObjectNotFoundException("could not find origin by id:" + originId));

		origin.addResponse(response);
		this.elementDao.save(origin);
	}
	// TODO

	@Override
	@Transactional(readOnly = true)
	public Set<ElementBoundary> getAllChildrenOfAnExsitingElement(String userDomain, String userEmail,
			String elementDomain, String elementId, int size, int page) {

		ServiceTools.validatePaging(size, page);

		ServiceTools.stringValidation(elementDomain, elementId, userDomain, userEmail);

		ElementIdEntity eid = new ElementIdEntity(elementDomain, elementId);

		ElementEntity element = this.elementDao.findById(eid).orElseThrow(() -> new ObjectNotFoundException(
				"could not find origin by domain: " + elementDomain + "and id: " + elementId));

		return element.getResponses().stream().map(this.converter::fromEntity).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getAnArrayWithElementParent(String userDomain, String userEmail,
			String elementDomain, String elementId, int size, int page) {
		ElementEntity child = this.elementDao.findById(new ElementIdEntity(elementDomain, elementId))
				.orElseThrow(() -> new ObjectNotFoundException("could not find response by id:" + elementId));

		/*if (size < 1) {
			throw new RuntimeException("size must be not less than 1");
		}

		if (page < 0) {
			throw new RuntimeException("page must not be negative");
		}*/
		
		ServiceTools.validatePaging(size, page);

		ElementEntity origin = child.getOrigin();
		Collection<ElementBoundary> rv = new HashSet<>();

		if (origin != null && page == 0) {
			ElementBoundary rvBoundary = this.converter.fromEntity(origin);
			rv.add(rvBoundary);
		}
		return rv;

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsByName(String userDomain, String userEmail, String name, int size,
			int page) {
		ServiceTools.stringValidation(userDomain, userEmail, name);
		ServiceTools.validatePaging(size, page);
		UserEntity uE = this.userDao.findById(new UserIdEntity(userDomain, userEmail))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find user by userDomain: " + userDomain + "and userEmail: " + userEmail));
		if (uE.getRole() == UserRoleEntityEnum.admin)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin User Can't Search Elements By Location");
		if (uE.getRole() == UserRoleEntityEnum.player)
			return this.elementDao
					.findAllByNameAndActive(name, true, PageRequest.of(page, size, Direction.DESC, "name")).stream()
					.map(this.converter::fromEntity).collect(Collectors.toList());
		if (uE.getRole() == UserRoleEntityEnum.manager)
			return this.elementDao.findAllByName(name, PageRequest.of(page, size, Direction.DESC, "name")).stream()
					.map(this.converter::fromEntity).collect(Collectors.toList());
		return new ArrayList<>();
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> searchByLocation(String userDomain, String userEmail, double lat, double lng,
			double distance, int size, int page) {

		ServiceTools.validatePaging(size, page);

		ServiceTools.stringValidation(userDomain, userEmail);

		UserEntity uE = this.userDao.findById(new UserIdEntity(userDomain, userEmail))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find user by userDomain: " + userDomain + "and userEmail: " + userEmail));

		if (uE.getRole() == UserRoleEntityEnum.manager)
			return this.elementDao
					.findAllByLocation_LatBetweenAndLocation_LngBetween(lat - distance, lat + distance, lng - distance,
							lng + distance, PageRequest.of(page, size, Direction.ASC, "name"))
					.stream().map(this.converter::fromEntity).collect(Collectors.toList());

		if (uE.getRole() == UserRoleEntityEnum.player)
			return this.elementDao
					.findAllByLocation_LatBetweenAndLocation_LngBetweenAndActive(lat - distance, lat + distance,
							lng - distance, lng + distance, true, PageRequest.of(page, size, Direction.ASC, "name"))
					.stream().map(this.converter::fromEntity).collect(Collectors.toList());

		if (uE.getRole() == UserRoleEntityEnum.admin)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin User Can't Search Elements By Location");

		return new ArrayList<>();
	}
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsByType(String userDomain , String userEmail, String type, int size, int page) {
		
		UserEntity uE = this.userDao.findById(new UserIdEntity(userDomain, userEmail))
				.orElseThrow(() -> new ObjectNotFoundException("could not find user by userDomain: "
						+ userDomain + " and userEmail: " + userEmail));

		if (uE.getRole() == UserRoleEntityEnum.manager) {
			return this.elementDao.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "name"))
				.stream()
				.map(this.converter::fromEntity)
				.collect(Collectors.toList());
		}
		if (uE.getRole() == UserRoleEntityEnum.player)
			return this.elementDao.findAllByTypeAndActive(type, true, PageRequest.of(page, size, Direction.ASC, "name"))
					.stream()
					.map(this.converter::fromEntity)
					.collect(Collectors.toList());

		if (uE.getRole() == UserRoleEntityEnum.admin) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin User Can't Search Elements By Type");

	}
		return new ArrayList<>();
	}




}