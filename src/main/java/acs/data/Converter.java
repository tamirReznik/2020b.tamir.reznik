package acs.data;

import org.springframework.stereotype.Component;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.user.UserBoundary;

@Component
public class Converter {

	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(entity.getElementId().split("#")[0], entity.getElementId().split("#")[1]), entity.getType(), entity.getName(),
				entity.getActive(), entity.getTimeStamp(), entity.getLocation(), entity.getElemntAttributes(),
				entity.getCreateBy());
		return eb;
	}

	public ElementEntity toEntity(ElementBoundary boundary) {

		ElementEntity eE = new ElementEntity(boundary.getElementId().getDomain() + "#" +  boundary.getElementId().getId(), boundary.getType(), boundary.getName(),
				boundary.getActive(), boundary.getTimeStamp(), boundary.getLocation(), boundary.getElemntAttributes(),
				boundary.getCreateBy());
		return eE;
	}
	// domain :abc  id: 123 -- > abc#123

	public UserBoundary fromEntity(UserEntity entity) {
		UserBoundary ub = new UserBoundary(entity.getUserId(), entity.getRole(), entity.getUsername(),
				entity.getAvatar());
		return ub;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity ue = new UserEntity(boundary.getUserId(), boundary.getRole(), boundary.getUsername(),
				boundary.getAvatar());
		return ue;
	}

	public ActionEntity toEntity(ActionBoundary actionBoundary) {

		String type = actionBoundary.getType() == null ? null : actionBoundary.getType().name();

		return new ActionEntity(actionBoundary.getActionId(), actionBoundary.getElementId(), type,
				actionBoundary.getElement(), actionBoundary.getTimestamp(), actionBoundary.getInvokedBy(),
				actionBoundary.getActionAttributes());

	}

	public ActionBoundary fromEntity(ActionEntity entity) {

		ActionBoundary boundary = new ActionBoundary(entity.getActionId(), null, entity.getElement(),
				entity.getTimestamp(), entity.getInvokedBy(), entity.getActionAttributes(), entity.getElementId());

		if (entity.getType() != null)
			boundary.setType(TypeEnum.valueOf(entity.getType()));

		return boundary;

	}

}
