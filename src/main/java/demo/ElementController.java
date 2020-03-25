package demo;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

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
public class ElementController {

	// Element related API - Create new element (avichai & tamir)
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary CreateNewElement(@RequestBody NewElementDetailsBoundary userDetails) {
		ElementBoundary eb = new ElementBoundary();
		eb.setActive(true);
		eb.setLocation(new Location(1545235.534, 92372.4573));
		eb.setName("Parking Lot");
		eb.setTimeStamp(new Date(0));
		eb.setType(TypeEnum.CRITICAL);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		eb.setElemntAttributes(tempMap);

		return eb;
	}
}
