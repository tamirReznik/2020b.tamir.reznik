package acs.rest.boundaries.action;

import java.util.Date;
import java.util.Map;

/*
{
    "actionId": {
        "domain": "2020b.tamir.reznik",
        "id": "ce44996c-9a9d-475a-ae53-27575e6d24b5"
    },
    "type": "actionType",
    "element": {
        "elementId": {
            "domain": "2020b.demo",
            "id": 0
        }
    },
    "createdTimestamp": "2020-04-27T16:13:57.475+0000",
    "invokedBy": {
        "userId": {
            "domain": "2020b.demo",
            "email": "anna@gmail.com"
        }
    },
    "actionAttributes": {
        "streetName": "Sheshet Hayamim",
        "cityName": "Binyamina",
        "isParkingEmpty": false,
        "nameOfParking": "parking name"
    }
}
 */

public class ActionBoundary {
	private ActionIdBoundary actionId;
	private String type;
	private ElementOfAction element;
	private Date createdTimestamp;
	private Map<String, Object> invokedBy;
	private Map<String, Object> actionAttributes;

	public ActionBoundary() {
	}

	public ActionBoundary(ActionIdBoundary actionId, String type, ElementOfAction element, Date createdTimestamp,
			Map<String, Object> invokedBy, Map<String, Object> actionAttributes) {

		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.setCreatedTimestamp(createdTimestamp);
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public ActionIdBoundary getActionId() {
		return actionId;
	}

	public void setActionId(ActionIdBoundary actionId) {
		this.actionId = actionId;
	}

	public ElementOfAction getElement() {
		return element;
	}

	public void setElement(ElementOfAction element) {
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

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
