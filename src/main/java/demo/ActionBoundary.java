package demo;

import java.util.Date;
import java.util.Map;
//Anna
public class ActionBoundary {
	private ActionIdBoundary actionId;
	private TypeEnum type;
	private Map<String,Object> element;
	private Date timeStap;
	private Map<String, Object> invokedBy;
	private ActionAttributes actionAttributes;
	
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
		return timeStap;
	}
	
	public void setTimeStap(Date timeStap) {
		this.timeStap = timeStap;
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
