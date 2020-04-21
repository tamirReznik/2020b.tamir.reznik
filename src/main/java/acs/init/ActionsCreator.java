package acs.init;

import java.util.HashMap;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import acs.data.TypeEnum;
import acs.logic.ActionService;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;

@Component
public class ActionsCreator implements CommandLineRunner {
	private ActionService actionService;

	public ActionsCreator(ActionService actionService) {
		this.actionService = actionService;
	}

	@Override
	public void run(String... args) throws Exception {
//		IntStream.range(1, 5)
//		.map(i-> new ActionBoundary(new ActionIdBoundary("test", Integer.toString(i)),TypeEnum.actionType, new HashMap<String, Object>, timeStap, invokedBy, actionAttributes, elementId))
		
	}
}
