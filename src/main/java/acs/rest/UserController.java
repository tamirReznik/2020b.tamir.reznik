package acs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import acs.logic.UserService;
import acs.rest.boundaries.NewUserDetailsBoundary;
import acs.rest.boundaries.UserBoundary;
import acs.rest.boundaries.UserIdBoundary;

@RestController
public class UserController {
	private UserService userService;

	@Autowired
	public UserController() {
	}

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	// Sapir - User related API - Login valid user
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary loginValidUser(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return this.userService.login(userDomain, userEmail);

//		return userService.login(userDomain, userEmail);
//		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()) {
//			// return new UserBoundary();
//			UserBoundary ub = new UserBoundary();
//			ub.setUserId(new UserIdBoundary(userDomain, userEmail));
//			ub.setRole(TypeEnumRole.PLAYER);
//			ub.setUsername("Demo User");
//			ub.setAvatar(";-)");
//			return ub;
//		} else {
//			throw new NameNotFoundException("Invalid user name/email");
//		}
	}

	// Sapir - User related API - Create a new user
	@RequestMapping(path = "/acs/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary CreateNewUser(@RequestBody NewUserDetailsBoundary userDetails) {
//		UserBoundary ub = new UserBoundary();
		return this.userService.createUser(new UserBoundary(new UserIdBoundary("", userDetails.getEmail()),
				userDetails.getRole(), userDetails.getUsername(), userDetails.getAvatar()));
		// new UserBoundary(new
		// UserIdBoundary("",userDetails.getEmail()),userDetails.getRole(),userDetails.getUsername(),userDetails.getAvatar())
//		System.out.println(userDetails.getEmail());
//		UserBoundary ub = new UserBoundary();
//		ub.setUserId(new UserIdBoundary("2020b.demo", userDetails.getEmail()));
//		ub.setRole(userDetails.getRole());
//		ub.setUsername(userDetails.getUsername());
//		ub.setAvatar(userDetails.getAvatar());
//		return null;
	}

	// Sapir - User related API - Update user details
	@RequestMapping(path = "/acs/users/{userDomain}/{userEmail}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @RequestBody UserBoundary update) {
		// TODO emplement updateUserDetails method
	}

}
