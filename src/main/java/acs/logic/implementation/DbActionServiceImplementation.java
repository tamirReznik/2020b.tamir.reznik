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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import acs.dal.ActionDao;
import acs.data.ActionEntity;
import acs.data.Converter;
import acs.logic.EnhancedActionService;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;

@Service
public class DbActionServiceImplementation implements EnhancedActionService {
	private String projectName;
	private ActionDao actionDao;
	private Converter converter;

	@Autowired
	public DbActionServiceImplementation(ActionDao actionDao, Converter converter) {
		this.converter = converter;
		this.actionDao = actionDao;
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
			ActionIdBoundary aib = new ActionIdBoundary(projectName, UUID.randomUUID().toString());
			action.setCreatedTimestamp(new Date());
			action.setActionId(aib);
			ActionEntity entity = converter.toEntity(action);
			// actionDao.put(action.getActionId().toString(), entity);
			this.actionDao.save(entity);
			return action;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {
			Iterable<ActionEntity> allActions = this.actionDao.findAll();
			List<ActionBoundary> rv = new ArrayList<>();
			for (ActionEntity ent : allActions)
				rv.add(this.converter.fromEntity(ent));
//			return this.actionDao.values().stream().map(this.converter::fromEntity).collect(Collectors.toList());
			return rv;
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}
	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllActions(String adminDomain, String adminEmail) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {
			this.actionDao.deleteAll();
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}

	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {

			if (size < 1) {
				throw new RuntimeException("size must be not less than 1");
			}

			if (page < 0) {
				throw new RuntimeException("page must positive");
			}

			return this.actionDao.findAll(PageRequest.of(page, size, Direction.DESC, "actionId"))// Page<ActionEntity>
					.getContent()// List<ActionEntity>
					.stream()// Stream<ActionEntity>
					.map(this.converter::fromEntity)// Stream<ActionEntity>
					.collect(Collectors.toList()); // List<ActionEntity>
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}
	}
}
