package acs.logic.implementation;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import acs.dal.ActionDao;
import acs.data.ActionEntity;
import acs.data.Converter;
import acs.logic.ActionService;
import acs.rest.boundaries.action.ActionBoundary;

public class DbActionServiceImplementation implements ActionService {

	private String projectName;
	private ActionDao actionDao;
	private Converter converter;

	@Autowired
	public DbActionServiceImplementation(Converter converter) {
		this.converter = converter;
	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional
	public Object invokeAction(ActionBoundary action) {
		if (action == null) {
			throw new RuntimeException("ActionBoundary received in invokeAction method can't be null\n");
		} else {
			action.setTimestamp(new Date());
			action.getActionId().setDomain(projectName);
			action.getActionId().setId(UUID.randomUUID().toString());
			ActionEntity entity = converter.toEntity(action);
			//actionDao.put(action.getActionId().toString(), entity);
			this.actionDao.save(entity);
			return action;
		}
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {
			return this.actionDao.values().stream().map(this.converter::fromEntity).collect(Collectors.toList());
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}

	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		if (adminDomain != null && !adminDomain.trim().isEmpty() && adminEmail != null
				&& !adminEmail.trim().isEmpty()) {
			this.actionDao.clear();
		} else {
			throw new RuntimeException("Admin Domain and Admin Email must not be empty or null");
		}

	}
}
