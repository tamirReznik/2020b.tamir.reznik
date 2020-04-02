package acs.rest;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.TypeEnum;
import acs.rest.boundaries.ActionAttributes;
import acs.rest.boundaries.ActionBoundary;
import acs.rest.boundaries.ActionIdBoundary;
import acs.rest.boundaries.ElementIdBoundary;
import acs.rest.boundaries.UserBoundary;
import acs.rest.boundaries.UserIdBoundary;

@RestController
public class AdminController {
	// Anna -Admin API -Export all users
	UserController uc = new UserController();
	ActionController ac = new ActionController();

	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> uc.loginValidUser(adminDomain, adminEmail)) // Stream of UserBoundry
				.collect(Collectors.toList()) // List of UserBoundry
				.toArray(new UserBoundary[0]); // ComplexMessagBoundary[]
	}

	// Anna -Admin API -Export all actions
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> ac.invokeAnAction(new ActionBoundary(new ActionIdBoundary(adminDomain, i),
						TypeEnum.CRITICAL,
						Collections.singletonMap("elementId", new ElementIdBoundary("2020b.demo", i)), new Date(),
						Collections.singletonMap("userId", new UserIdBoundary("2020b.demo", "test@gmail.com")),
						new ActionAttributes("Rotchsild", "Tel Aviv", false, "TLVParking"), new ElementIdBoundary()))) // Stream
																														// of
																														// UserBoundry
				.collect(Collectors.toList()) // List of UserBoundry
				.toArray(new ActionBoundary[0]);// ComplexMessagBoundary[]
	}

//	http DELETE method - delete all users(tamir)
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		// return type is void - change after proper method implementation!
		// TODO implement delete all users method
	}

//	http DELETE method - delete all elements(tamir)
	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		// return type is void - change after proper method implementation!

	}

//	http DELETE method - delete all actions(tamir)
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		// return type is void - change after proper method implementation!
		// TODO implement delete all actions method
	}
}
