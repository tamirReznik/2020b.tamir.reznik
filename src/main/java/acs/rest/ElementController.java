package acs.rest;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.TypeEnum;
import acs.TypeEnumRole;
import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.ElementIdBoundary;
import acs.rest.boundaries.Location;
import acs.rest.boundaries.UserBoundary;
import acs.rest.boundaries.UserIdBoundary;

@RestController
public class ElementController {

//	http POST method - Create (& returns) new element (avichai & tamir)

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary CreateNewElement(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @RequestBody ElementBoundary elementDetails) {

		elementDetails.setElementId(new ElementIdBoundary("avichai", 3083462));
		return elementDetails;
	}

//	http GET method - returns element via domain & id(tamir)
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary RetreiveElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") int elementId) {

		return CreateNewElement("managerDomain", "managerEmail",
				new ElementBoundary(new ElementIdBoundary(userDomain, elementId), TypeEnum.CRITICAL, "avichai", true,
						new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
						Collections.singletonMap("created by", "user")));
	}

//	http GET method - returns Array Of Elements(tamir)
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] RetreiveElementArr(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> CreateNewElement("managerDomain", "managerEmail",
						new ElementBoundary(new ElementIdBoundary(userDomain, i), TypeEnum.CRITICAL, "avichai",
								true, new Date(0), new Location(4.5, 3.6), Collections.singletonMap("key", "value"),
								Collections.singletonMap("created by", "user")))) // Stream
																					// of
																					// ElementBoundary
				.collect(Collectors.toList()) // List of ElementBoundry
				.toArray(new ElementBoundary[0]);

	}

//	http PUT method - update element(tamir)
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId, @RequestBody ElementBoundary update) {
		// TODO implement this method to update element details
	}
}
