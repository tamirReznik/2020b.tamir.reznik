package demo;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	// Anna -Admin API -Export all users
	UserController uc = new UserController();

	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] users(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> uc.loginValidUser(adminDomain, adminEmail)) // Stream of UserBoundry
				.collect(Collectors.toList()) // List of UserBoundry
				.toArray(new UserBoundary[0]); // ComplexMessagBoundary[]
	}

}
