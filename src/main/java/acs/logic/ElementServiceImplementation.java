package acs.logic;

import java.sql.Date;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import acs.TypeEnum;
import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.ElementIdBoundary;
import acs.rest.boundaries.Location;

public class ElementServiceImplementation implements ElementService {

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary elementDetails) {
		elementDetails.setElementId(new ElementIdBoundary("avichai", "3083462"));
		return elementDetails;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		update.getElementId().setDomain(elementDomain);
		update.getElementId().setId(elementId);
		return update;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> create("managerDomain", "managerEmail",
						new ElementBoundary(new ElementIdBoundary(userDomain, String.valueOf(i)), TypeEnum.CRITICAL, "avichai", true,
								new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
								Collections.singletonMap("created by", "user")))) // Stream
																					// of
																					// ElementBoundary
				.collect(Collectors.toList()); // List of ElementBoundry

	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		create("managerDomain", "managerEmail",
				new ElementBoundary(new ElementIdBoundary(userDomain, elementId), TypeEnum.CRITICAL,
						"avichai", true, new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
						Collections.singletonMap("created by", "user")));
		return null;
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub

	}

}
