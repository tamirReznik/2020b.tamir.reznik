package acs.logic.implementation;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import acs.data.TypeEnum;
import acs.logic.ActionService;
import acs.rest.boundaries.action.ActionAttributes;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;

@Service
public class ActionServiceImplementation implements ActionService {

	@Override
	public Object invokeAction(ActionBoundary action) {
		return "Action ID:" + action.getActionId().getId() + " invoked by - " + action.getInvokedBy().toString();
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		Map<String, Object> invokeBy = new HashMap<String, Object>();
		invokeBy.put(adminDomain, adminEmail);

//		return Arrays.asList(
//				new ActionBoundary(new ActionIdBoundary("Yonatan", "1"), TypeEnum.demoElement,
//						new HashMap<String, Object>(), new Date(), invokeBy, new Map<String, Object>(),
//						new ElementIdBoundary()),
//				new ActionBoundary(new ActionIdBoundary("Anna", "2"), TypeEnum.demoElement,
//						new HashMap<String, Object>(), new Date(), invokeBy, null, new ElementIdBoundary()),
//				new ActionBoundary(new ActionIdBoundary("Sapir", "3"), TypeEnum.demoElement,
//						new HashMap<String, Object>(), new Date(), invokeBy, null, new ElementIdBoundary()),
//				new ActionBoundary(new ActionIdBoundary("Avichai", "4"), TypeEnum.demoElement,
//						new HashMap<String, Object>(), new Date(), invokeBy, null, new ElementIdBoundary()),
//				new ActionBoundary(new ActionIdBoundary("Tamir", "5"), TypeEnum.demoElement,
//						new HashMap<String, Object>(), new Date(), invokeBy, null, new ElementIdBoundary()));
		return null;
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated Delete method stub

	}

}
