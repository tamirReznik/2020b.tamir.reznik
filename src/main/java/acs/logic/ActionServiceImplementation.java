package acs.logic;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;

import acs.MessageBoundary;
import acs.TypeEnum;
import acs.rest.boundaries.ActionAttributes;
import acs.rest.boundaries.ActionBoundary;
import acs.rest.boundaries.ActionIdBoundary;
import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.ElementIdBoundary;

@Service
public class ActionServiceImplementation implements ActionService {

	@Override
	public Object invokeAction(ActionBoundary action) {
		return new MessageBoundary(
				"Action ID:" + action.getActionId().getId() + " invoked by - " + action.getInvokedBy().toString());
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		Map<String, Object> invokeBy = new HashMap<String, Object>();
		invokeBy.put(adminDomain, adminEmail);
		return Arrays.asList(
				new ActionBoundary(new ActionIdBoundary("Yonatan", 1), TypeEnum.CRITICAL, new HashMap<String, Object>(),
						new Date(), invokeBy, new ActionAttributes(), new ElementIdBoundary()),
				new ActionBoundary(new ActionIdBoundary("Anna", 2), TypeEnum.CRITICAL, new HashMap<String, Object>(),
						new Date(), invokeBy, new ActionAttributes(), new ElementIdBoundary()),
				new ActionBoundary(new ActionIdBoundary("Sapir", 3), TypeEnum.CRITICAL, new HashMap<String, Object>(),
						new Date(), invokeBy, new ActionAttributes(), new ElementIdBoundary()),
				new ActionBoundary(new ActionIdBoundary("Avichai", 4), TypeEnum.CRITICAL, new HashMap<String, Object>(),
						new Date(), invokeBy, new ActionAttributes(), new ElementIdBoundary()),
				new ActionBoundary(new ActionIdBoundary("Tamir", 5), TypeEnum.CRITICAL, new HashMap<String, Object>(),
						new Date(), invokeBy, new ActionAttributes(), new ElementIdBoundary()));
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated Delete method stub

	}

}
