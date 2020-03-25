package demo;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	@RequestMapping(path = "/hello/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageBoundary helloUser(@PathVariable("name") String name) {
		if (name != null && !name.trim().isEmpty()) {
			return new MessageBoundary("Hello " + name + "!");
		} else {
			throw new NameNotFoundException("Invalid name");
		}
	}

//User related API - Login valid user 	

	// Sapir

//	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public UserBoundary loginValidUser(@PathVariable("userDomain") String userDomain,
//			@PathVariable("userEmail") String userEmail) {
//		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()) {
//			// return new UserBoundary();
//			UserBoundary ub = new UserBoundary();
//			ub.setUserId(new UserIdBoundary(userDomain, userEmail));
//			ub.setTypeRole(TypeEnumRole.PLAYER); // ???
//			ub.setUsername("Demo User"); // ???
//			ub.setAvater(";-)"); // ???
//			return ub;
//		} else {
//			throw new NameNotFoundException("Invalid user name/email");
//		}
//	}

//	// Anna -Admin API -Export all users
//	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public UserBoundary[] users(@PathVariable("adminDomain") String adminDomain,
//			@PathVariable("adminEmail") String adminEmail) {
//
//		UserController uc = new UserController();
//		return IntStream.range(0, 5) // Stream of Integer
//				.mapToObj(i -> uc.loginValidUser(adminDomain, adminEmail)) // Stream of UserBoundry
//				.collect(Collectors.toList()) // List of UserBoundry
//				.toArray(new UserBoundary[0]); // ComplexMessagBoundary[]
//	}

//User related API - Create a new user
//	@RequestMapping(path = "/acs/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public UserBoundary CreateNewUser(@RequestBody NewUserDetailsBoundary userDetails) {
//		UserBoundary ub = new UserBoundary();
//		ub.setUserId(new UserIdBoundary("2020b.demo", userDetails.getEmail())); // ???
//		ub.setTypeRole(userDetails.getTypeRole());
//		ub.setUsername(userDetails.getUsername());
//		ub.setAvater(userDetails.getAvater());
//		return ub;
//	}

	@RequestMapping(path = "/echo/{suffix}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public MessageBoundary echo(@PathVariable("suffix") boolean suffix, @RequestBody MessageBoundary message) {
		if (suffix) {
			return new MessageBoundary(message.getMessage() + " suffix: BLA BLA BLA");
		} else {
			return message;
		}
	}

	@RequestMapping(path = "/echo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ComplexMessagBoundary echo(@RequestBody MessageBoundary message) {
		ComplexMessagBoundary rv = new ComplexMessagBoundary();
		rv.setMessage(message.getMessage());
		rv.setDetails(Collections.singletonMap("dummy", 92));
		rv.setTimestamp(new Date());
		rv.setImportant(false);
		rv.setType(TypeEnum.NON_CRITICAL);
		rv.setValue(9.99);
		rv.setName(new NameBoundary("Demo", "Test"));
		return rv;
	}

	@RequestMapping(path = "/echo/many/{count}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ComplexMessagBoundary[] echoMany(@RequestBody MessageBoundary message, @PathVariable("count") int count) {
//		Random rand = new Random(System.currentTimeMillis());
//		ComplexMessagBoundary[] rv = new ComplexMessagBoundary[count];
//		for (int i = 0; i < count; i++) {
//			rv[i] = echo(message);
//			Map<String, Object> details = new HashMap<>();
//			details.putAll(rv[i].getDetails());
//			details.put("randomValue", rand.nextInt(10));
//			rv[i].setDetails(details);
//		}
//		return rv;
		Random rand = new Random(System.currentTimeMillis());
		return IntStream.range(0, count) // Stream of Integer
				.mapToObj(i -> echo(message)) // Stream of ComplexMessagBoundary
				.map(msg -> {
					Map<String, Object> details = new HashMap<>();
					details.putAll(msg.getDetails());
					details.put("randomValue", rand.nextInt(10));
					msg.setDetails(details);
					return msg;
				}) // Stream of ComplexMessagBoundary
				.collect(Collectors.toList()) // List of ComplexMessagBoundary
				.toArray(new ComplexMessagBoundary[0]); // ComplexMessagBoundary[]
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleException(NameNotFoundException e) {
		String error = e.getMessage();
		if (error == null) {
			error = "name not found";
		}

		return Collections.singletonMap("error", error);
	}
}
