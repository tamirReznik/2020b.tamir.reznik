package acs.rest.boundaries.action;

import java.util.Date;
import java.util.Map;
//Anna

import acs.data.TypeEnum;
import acs.rest.boundaries.element.ElementIdBoundary;

/*
{
 	"actionId":{
		"domain": "tamir",
		"id": "54"	
	}, 	
	"type":"CRITICAL",
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
	private ElementIdBoundary elementId;
	private String type;
	private Map<String, Object> element;
	private Date timestamp;
	private Map<String, Object> invokedBy;
//	private ActionAttributes actionAttributes;
	private Map<String, Object> actionAttributes;

	public ActionBoundary() {
	}

	public ActionBoundary(ActionIdBoundary actionId, String type, Map<String, Object> element, Date timeStap,
			Map<String, Object> invokedBy, Map<String, Object> actionAttributes, ElementIdBoundary elementId) {
		this.elementId = elementId;
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.timestamp = timeStap;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ActionIdBoundary getActionId() {
		return actionId;
	}

	public void setActionId(ActionIdBoundary actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getElement() {
		return element;
	}

	public void setElement(Map<String, Object> element) {
		this.element = element;
	}

	public ElementIdBoundary getElementId() {
		return elementId;
	}

	public void setElementId(ElementIdBoundary elementId) {
		this.elementId = elementId;
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
