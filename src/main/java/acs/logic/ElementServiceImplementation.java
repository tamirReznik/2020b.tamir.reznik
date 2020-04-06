package acs.logic;

import java.sql.Date;
import java.util.Collections;
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
import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.ElementIdBoundary;




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
		elementDetails.setElementId(new ElementIdBoundary(managerDomain, UUID.randomUUID().toString()));
		
		ElementEntity entity = this.converter.toEntity(elementDetails);
		entity.setTimeStamp(new Date(0));
		this.elementDatabase.put(entity.getElementId().getId(), entity);
		return this.converter.fromEntity(entity);

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		ElementEntity existing = this.elementDatabase.get(elementId);
		if(existing==null)
			throw new ElementNotFoundException("could not find object by id:" + elementId);
		else {
			if(update.getActive()!=null)
				existing.setActive(update.getActive());
			if(update.getName()!=null)
				existing.setName(update.getName());
			if(update.getLocation()!=null)
				existing.setLocation(update.getLocation());
			if(update.getType()!=null)
				existing.setType(update.getType());
			return this.converter
					.fromEntity(existing);
		}
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		
		return this.elementDatabase // Map<String, DummyEntity>
				.values()           // Collection<DummyEntity>
				.stream()		    // Stream<DummyEntity>			
				.map(this.converter::fromEntity)	// Stream<DummyBoundaries>
//				.map(e->this.converter.fromEntity(e))	// Stream<DummyBoundaries>		
				.collect(Collectors.toList()); // List<DummyBoundaries>
//		return IntStream.range(0, 5) // Stream of Integer
//				.mapToObj(i -> create("managerDomain", "managerEmail",
//						new ElementBoundary(new ElementIdBoundary(userDomain, String.valueOf(i)), TypeEnum.CRITICAL, "avichai", true,
//								new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
//								Collections.singletonMap("created by", "user")))) // Stream
//																					// of
//																					// ElementBoundary
//				.collect(Collectors.toList()); // List of ElementBoundry

	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		ElementEntity existing = this.elementDatabase.get(elementId);
		if (existing != null) {
			return this.converter
				.fromEntity(
					existing);
		}else {
			throw new ElementNotFoundException("could not find object by id: " + elementId);
		}
		
//		create("managerDomain", "managerEmail",
//				new ElementBoundary(new ElementIdBoundary(userDomain, elementId), TypeEnum.CRITICAL,
//						"avichai", true, new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
//						Collections.singletonMap("created by", "user")));
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDatabase.clear();

	}

}
