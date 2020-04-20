package acs.data;

import java.util.Date;
import java.util.Map;

public class ActionEntity {

	private String actionId;
	private String elementId;
	private String type;
	private Map<String, Object> element;
	private Date timestamp;
	private Map<String, Object> invokedBy;
	private Map<String, Object> actionAttributes;

	public ActionEntity() {
	}

	public ActionEntity(String actionId, String elementId, String type, Map<String, Object> element, Date timestamp,
			Map<String, Object> invokedBy, Map<String, Object> actionAttributes) {
		super();
		this.actionId = actionId;
		this.elementId = elementId;
		this.type = type;
		this.element = element;
		this.timestamp = timestamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public String getElementId() {
		return elementId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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