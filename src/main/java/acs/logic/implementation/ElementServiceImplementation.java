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
import org.springframework.stereotype.Service;
import acs.data.Converter;
import acs.data.ElementEntity;
import acs.logic.ElementService;
import acs.logic.ObjectNotFoundException;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;

@Service
public class ElementServiceImplementation implements ElementService {
	private Map<String, ElementEntity> elementDatabase;
	private Converter converter;

	@Autowired
	public ElementServiceImplementation(Converter converter) {
		this.converter = converter;
	}

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.elementDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary elementDetails) {
		elementDetails.setElementId(new ElementIdBoundary(managerDomain, UUID.randomUUID().toString()));// replace
																										// managerdomain
																										// with project
																										// name
		ElementEntity entity = this.converter.toEntity(elementDetails);
		entity.setTimeStamp(new Date());
		this.elementDatabase.put(entity.getElementId().getId(), entity);
		return this.converter.fromEntity(entity);

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		ElementEntity existing = this.elementDatabase.get(elementId);
		if (existing == null)
			throw new ObjectNotFoundException("could not find object by id:" + elementId);
		else {
			if (update.getActive() != null)
				existing.setActive(update.getActive());
			if (update.getName() != null)
				existing.setName(update.getName());
			if (update.getLocation() != null)
				existing.setLocation(update.getLocation());
			if (update.getType() != null)
				existing.setType(update.getType());
			return this.converter.fromEntity(existing);
		}
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {// check for null in arguments

		return this.elementDatabase.values().stream().map(this.converter::fromEntity).collect(Collectors.toList());
	}

	@Override // check for null in arguments
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		ElementEntity existing = this.elementDatabase.get(elementId);
		if (existing != null) {
			return this.converter.fromEntity(existing);
		} else {
			throw new ObjectNotFoundException("could not find object by id: " + elementId);
		}

	}

	@Override // check for null in arguments
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDatabase.clear();

	}

}
