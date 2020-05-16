package acs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import acs.logic.ElementService;
import acs.logic.EnhancedActionService;
import acs.logic.UserService;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.user.UserBoundary;

@RestController
public class AdminController {

	private UserService userService;
	private EnhancedActionService actionService;
	private ElementService elementService;

	@Autowired
	public AdminController() {
	}

	public AdminController(UserService userService, EnhancedActionService actionService,
			ElementService elementService) {
		super();
		this.userService = userService;
		this.actionService = actionService;
		this.elementService = elementService;
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setActionService(EnhancedActionService actionService) {
		this.actionService = actionService;
	}

	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return this.userService.getAllUsers(adminDomain, adminEmail).toArray(new UserBoundary[0]);

	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return actionService.getAllActions(adminDomain, adminEmail, size, page).toArray(new ActionBoundary[0]);
	}

	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		userService.deleteAllUsers(adminDomain, adminEmail);
	}

	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		elementService.deleteAllElements(adminDomain, adminEmail);

	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		actionService.deleteAllActions(adminDomain, adminEmail);
	}
}
