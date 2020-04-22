package acs.rest.boundaries.action;

import java.util.Date;
import java.util.Map;
import acs.data.TypeEnum;

/*
{
 	"actionId":{
		"domain": "tamir",
		"id": "54"	
	}, 	

	"type":"actionType",
	"element":{
		"elementId": {
            "domain": "2020b.demo",
            "id": 0
        }
	},
 	"invokedBy":{
 		"userId": {
            "domain": "2020b.demo",
            "email": "anna@gmail.com"
        }
	},
	"actionAttributes":{
		"streetName": "Sheshet Hayamim",
		"cityName": "Binyamina",	
		"isParkingEmpty": false,
		"nameOfParking": "parking name"
	} 	
}
 */

public class ActionBoundary {
	private ActionIdBoundary actionId;
	private TypeEnum type;
	private Map<String, Object> element;
	private Date createdTimestamp;
	private Map<String, Object> invokedBy;
	private Map<String, Object> actionAttributes;

	public ActionBoundary() {
	}

	public ActionBoundary(ActionIdBoundary actionId, TypeEnum type, Map<String, Object> element, Date timeStap,
			Map<String, Object> invokedBy, Map<String, Object> actionAttributes) {

		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimestamp = timeStap;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public Date getTimestamp() {
		return createdTimestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.createdTimestamp = timestamp;
	}

	public ActionIdBoundary getActionId() {
		return actionId;
	}

	public void setActionId(ActionIdBoundary actionId) {
		this.actionId = actionId;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public Map<String, Object> getElement() {
		return element;
	}

	public void setElement(Map<String, Object> element) {
		this.element = element;
	}

	public Map<String, Object> getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(Map<String, Object> invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

}
