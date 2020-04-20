package acs.data;

import org.springframework.stereotype.Component;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.user.UserBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

@Component
public class Converter {

	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary eb = new ElementBoundary(entity.getElementId(), entity.getType(), entity.getName(),
				entity.getActive(), entity.getTimeStamp(), entity.getLocation(), entity.getElemntAttributes(),
				entity.getCreateBy());
		return eb;
	}

	public ElementEntity toEntity(ElementBoundary boundary) {

		ElementEntity eE = new ElementEntity(boundary.getElementId(), boundary.getType(), boundary.getName(),
				boundary.getActive(), boundary.getTimeStamp(), boundary.getLocation(), boundary.getElemntAttributes(),
				boundary.getCreateBy());
		return eE;
	}

	public UserBoundary fromEntity(UserEntity entity) {
		UserIdBoundary userId = new UserIdBoundary(entity.getUserId().substring(0, entity.getUserId().indexOf('#')),
				entity.getUserId().substring(entity.getUserId().indexOf('#') + 1));

		UserBoundary ub = new UserBoundary(userId, null, entity.getUsername(), entity.getAvatar());

		if (entity.getRole() != null)
			ub.setRole(UserRole.valueOf(entity.getRole()));

		return ub;
	}

	public UserEntity toEntity(UserBoundary boundary) {

		String role = boundary.getRole() == null ? null : boundary.getRole().name();
		String userId = boundary.getUserId().toString();

		UserEntity ue = new UserEntity(userId, role, boundary.getUsername(), boundary.getAvatar());

		return ue;
	}

	public String toEntity(UserRole type) {
		if (type != null) {
			return type.name();
		} else {
			return null;
		}
	}

	public ActionEntity toEntity(ActionBoundary actionBoundary) {

		String type = actionBoundary.getType() == null ? null : actionBoundary.getType().name();

		return new ActionEntity(fromIdBoundary(actionBoundary.getActionId()),
				fromIdBoundary(actionBoundary.getElementId()), type, actionBoundary.getElement(),
				actionBoundary.getTimestamp(), actionBoundary.getInvokedBy(), actionBoundary.getActionAttributes());

	}

	public ActionIdBoundary toActionIdBoundary(String entity) {
		if (entity != null && !entity.trim().isEmpty())
			return new ActionIdBoundary(entity.substring(0, entity.indexOf('#')),
					entity.substring(entity.indexOf('#') + 1));

		return null;
	}

	public ElementIdBoundary toElementIdBoundary(String entity) {
		if (entity != null && !entity.trim().isEmpty())
			return new ElementIdBoundary(entity.substring(0, entity.indexOf('#')),
					entity.substring(entity.indexOf('#') + 1));

		return null;
	}

	public ActionBoundary fromEntity(ActionEntity entity) {

		ActionIdBoundary actionIdBoundary = toActionIdBoundary(entity.getActionId());

		ElementIdBoundary elementIdBoundary = toElementIdBoundary(entity.getElementId());

		ActionBoundary boundary = new ActionBoundary(actionIdBoundary, null, entity.getElement(), entity.getTimestamp(),
				entity.getInvokedBy(), entity.getActionAttributes(), elementIdBoundary);

		if (entity.getType() != null)
			boundary.setType(TypeEnum.valueOf(entity.getType()));

		return boundary;

	}

}
