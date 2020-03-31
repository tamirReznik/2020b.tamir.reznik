package demo.action;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import demo.element.ElementIdBoundary;

@RestController
public class ActionController {
//Anna - actions related API - invoke an action
	ElementIdBoundary eb = new ElementIdBoundary();

	@RequestMapping(path = "/acs/actions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invokeAnAction(@RequestBody ActionBoundary actionDetails) {

		actionDetails.setActionId(new ActionIdBoundary(actionDetails.getActionId().getDomain(), 31567));
		actionDetails.getElement().put("elementId", new ElementIdBoundary("test", 0));

		return actionDetails;
	}
}
