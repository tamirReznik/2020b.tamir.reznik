package demo;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionController {
//Anna - actions related API - invoke an action
	@RequestMapping(path = "/acs/actions", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invokeAnAction(@RequestBody ActionBoundary actionDetails) {
		ActionBoundary ab = new ActionBoundary();
		ab.setActionId(new ActionIdBoundary("2020b.demo",0));
		ab.setType(TypeEnum.NON_CRITICAL);
		ab.setElement(Collections.singletonMap("elementId", new ElementIdBoundary("2020b.demo", 0)));
		ab.setTimeStap(new Date());
		ab.setInvokedBy(Collections.singletonMap("userId",new UserIdBoundary("2020b.demo", "test@gmail.com")));
		ab.setActionAttributes(new ActionAttributes("Rotchsild","Tel Aviv",false,"TLVParking"));
		return ab;
	}
}
