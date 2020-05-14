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

import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
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

		if (managerDomain != null && !managerDomain.trim().isEmpty() && managerEmail != null
				&& !managerEmail.trim().isEmpty()) {

			UserIdEntity uib = new UserIdEntity(managerDomain, managerEmail);
			UserEntity existing = this.userDao.findById(uib).orElseThrow(() -> new ObjectNotFoundException(
					"could not find object by UserDomain:" + managerDomain + "or userEmail:" + managerEmail));

			if (existing.getRole().equals(UserRoleEntityEnum.manager)) {

				elementDetails.setElementId(new ElementIdBoundary(projectName, UUID.randomUUID().toString()));
				ElementEntity entity = this.converter.toEntity(elementDetails);
				entity.setTimeStamp(new Date());
				Map<String, UserIdBoundary> createdBy = new HashMap<>();
				createdBy.put("userId", new UserIdBoundary(managerDomain, managerEmail));
				entity.setCreateBy(createdBy);
				return this.converter.fromEntity(this.elementDao.save(entity));
			}

			else
				throw new ObjectNotFoundException("You are not manager! Can't create an element");

		} else {
			throw new ObjectNotFoundException("User Domain and User Email must not be empty or null");
		}
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		if (elementDomain != null && !elementDomain.trim().isEmpty() && elementId != null && !elementId.trim().isEmpty()
				&& managerDomain != null && !managerDomain.trim().isEmpty() && managerEmail != null
				&& !managerEmail.trim().isEmpty()) {

			UserIdEntity uib = new UserIdEntity(managerDomain, managerEmail);
			UserEntity existingUser = this.userDao.findById(uib).orElseThrow(() -> new ObjectNotFoundException(
					"could not find object by UserDomain:" + managerDomain + "or userEmail:" + managerEmail));

			if (existingUser.getRole().equals(UserRoleEntityEnum.manager)) {

				ElementIdEntity elementIdEntity = new ElementIdEntity(elementDomain, elementId);
				ElementEntity existing = this.elementDao.findById(elementIdEntity)
						.orElseThrow(() -> new ObjectNotFoundException("could not find object by elementDomain: "
								+ elementDomain + "or elementId: " + elementId));

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

			} else
				throw new ObjectNotFoundException("You are not manager! Can't update an element");

		} else {
			throw new RuntimeException("User Domain and User Email must not be empty or null");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()) {

			Iterable<ElementEntity> allElements = this.elementDao.findAll();
			List<ElementBoundary> returnElements = new ArrayList<>();

			for (ElementEntity entity : allElements) {
				returnElements.add(this.converter.fromEntity(entity)); // map entities to boundaries
			}
			return returnElements;

		} else {
			throw new RuntimeException("User Domain and User Email must not be empty or null");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page) {

		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()) {

			if (size < 1) {
				throw new RuntimeException("size must be not less than 1");
			}

			if (page < 0) {
				throw new RuntimeException("page must not be negative");
			}

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

		} else {
			throw new RuntimeException("User Domain and User Email must not be empty or null");
		}

		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()
				&& elementDomain != null && !elementDomain.trim().isEmpty() && elementId != null
				&& !elementId.trim().isEmpty()) {

			ElementIdEntity elementIdEntity = new ElementIdEntity(elementDomain, elementId);
			ElementEntity existing = this.elementDao.findById(elementIdEntity)
					.orElseThrow(() -> new ObjectNotFoundException(
							"could not find object by elementDomain: " + elementDomain + "or elementId: " + elementId));
			if (existing != null) {
				return this.converter.fromEntity(existing);
			} else {
				throw new ObjectNotFoundException("could not find object by id: " + elementId);
			}
		} else {
			throw new RuntimeException("User Domain and User Email must not be empty or null");
		}

	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {
			this.elementDao.deleteAll();
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}
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

	@Override
	@Transactional(readOnly = true)
	public Set<ElementBoundary> getAllChildrenOfAnExsitingElement(String userDomain, String userEmail,
			String elementDomain, String elementId) {
		ElementIdEntity eid = new ElementIdEntity(elementDomain, elementId);

		ElementEntity origin = this.elementDao.findById(eid).orElseThrow(() -> new ObjectNotFoundException(
				"could not find origin by domain: " + elementDomain + "and id: " + elementId));

		return origin.getResponses().stream().map(this.converter::fromEntity).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getAnArrayWithElementParent(String userDomain, String userEmail,
			String elementDomain, String elementId) {

		ElementEntity element = this.elementDao.findById(new ElementIdEntity(elementDomain, elementId))
				.orElseThrow(() -> new ObjectNotFoundException(
						"could not find children by domain: " + elementDomain + "and id: " + elementId));

		ElementEntity parent = element.getOrigin();

		Collection<ElementBoundary> rv = new HashSet<>();
		if (parent != null) {
			ElementBoundary rvBoundary = this.converter.fromEntity(parent);
			rv.add(rvBoundary);
		}

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> searchByLocation(UserIdBoundary userIdBoundary, double lat, double lng,
			double distance, int size, int page) {

//			TODO - check how to search for distance
		UserEntity uE = this.userDao.findById(new UserIdEntity(userIdBoundary.getDomain(), userIdBoundary.getEmail()))
				.orElseThrow(() -> new ObjectNotFoundException("could not find user by userDomain: "
						+ userIdBoundary.getDomain() + "and userEmail: " + userIdBoundary.getEmail()));

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
//
		if (uE.getRole() == UserRoleEntityEnum.admin) {
//			TODO THROW the match 4** error

		}
		return new ArrayList<>();
	}
}