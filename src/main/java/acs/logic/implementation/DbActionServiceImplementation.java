package acs.logic.implementation;

import java.util.ArrayList;
import java.util.Date;
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

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.data.ActionEntity;
import acs.data.Converter;

import acs.data.ElementEntity;
import acs.data.ElementIdEntity;
import acs.data.UserRole;
import acs.logic.ActionService;

import acs.logic.EnhancedActionService;

import acs.logic.ObjectNotFoundException;

import acs.logic.ServiceTools;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.user.UserBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

@Service
public class DbActionServiceImplementation implements EnhancedActionService {
	private String projectName;
	private ActionDao actionDao;
	private Converter converter;
	private ElementDao elementDao;

	@Autowired
	public DbActionServiceImplementation(ActionDao actionDao, ElementDao elementDao, Converter converter) {
		this.converter = converter;
		this.actionDao = actionDao;
		this.elementDao = elementDao;

	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional // (readOnly = false)
	public Object invokeAction(ActionBoundary action) {
		if (action == null || action.getType() == null) {
			throw new RuntimeException("ActionBoundary received in invokeAction method can't be null\n");

		} else {

			/*for (Object user : action.getInvokedBy().values()) {
				UserBoundary userB = (UserBoundary) user;
				if (!userB.getRole().equals(UserRole.PLAYER))
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
							"Admin User Can't Search Elements By Location");
			}*/
		//	UserIdBoundary ub = (UserIdBoundary)action.getInvokedBy().values().toArray()[0];
			ElementIdEntity elementIdOfAction = this.converter.fromElementIdBoundary(action.getElement().getElement());
			ElementEntity element = this.elementDao.findById(elementIdOfAction)
					.orElseThrow(() -> new ObjectNotFoundException("could not find object by ElementDomain:"
							+ elementIdOfAction.getDomain() + " or ElementId:" + elementIdOfAction.getId()));

			if (element.getActive()) {
				ActionIdBoundary aib = new ActionIdBoundary(projectName, UUID.randomUUID().toString());
				action.setCreatedTimestamp(new Date());
				action.setActionId(aib);
				ActionEntity entity = converter.toEntity(action);
				// actionDao.put(action.getActionId().toString(), entity);
				this.actionDao.save(entity);
				return action;
			}
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin User Can't Search Elements By Location");
		}
		/*
		 * ActionIdBoundary aib = new ActionIdBoundary(projectName,
		 * UUID.randomUUID().toString()); action.setCreatedTimestamp(new Date());
		 * action.setActionId(aib); ActionEntity entity = converter.toEntity(action); //
		 * actionDao.put(action.getActionId().toString(), entity);
		 * this.actionDao.save(entity); return action;
		 */
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {

		ServiceTools.stringValidation(adminDomain, adminEmail);
		Iterable<ActionEntity> allActions = this.actionDao.findAll();
		List<ActionBoundary> rv = new ArrayList<>();
		for (ActionEntity ent : allActions)
			rv.add(this.converter.fromEntity(ent));
//			return this.actionDao.values().stream().map(this.converter::fromEntity).collect(Collectors.toList());
		return rv;

	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllActions(String adminDomain, String adminEmail) {

		ServiceTools.stringValidation(adminDomain, adminEmail);
		this.actionDao.deleteAll();

	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		ServiceTools.validatePaging(size, page);

		return this.actionDao.findAll(PageRequest.of(page, size, Direction.DESC, "actionId"))// Page<ActionEntity>
				.getContent()// List<ActionEntity>
				.stream()// Stream<ActionEntity>
				.map(this.converter::fromEntity)// Stream<ActionEntity>
				.collect(Collectors.toList()); // List<ActionEntity>

	}
}
