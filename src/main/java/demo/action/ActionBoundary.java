package demo.action;

import java.util.Date;
import java.util.Map;
//Anna

import demo.TypeEnum;

/*
{

 	{
	"domain": "tamir",
		"id": "54"	
	}, 	
	"type":"CRITICAL",
	"element":{
	
	},
	"timestamp":"20200327T161001",
 	"invokedBy":{
	
	},
	{
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
	private Date timestamp;
	private Map<String, Object> invokedBy;
	private ActionAttributes actionAttributes;

	public ActionBoundary(ActionIdBoundary actionId, TypeEnum type, Map<String, Object> element, Date timeStap,
			Map<String, Object> invokedBy, ActionAttributes actionAttributes) {
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.timestamp = timeStap;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
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

	public Date getTimeStap() {
		return timestamp;
	}

	public void setTimeStap(Date timeStap) {
		this.timestamp = timeStap;
	}

	public Map<String, Object> getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(Map<String, Object> invokedBy) {
		this.invokedBy = invokedBy;
	}

	public ActionAttributes getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(ActionAttributes actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

}
