package acs.data;

import java.util.Date;
import java.util.Map;

import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

public class ElementEntity {
	private String elementId;
	private TypeEnum type;
	private String name;
	private Boolean active;
	private Date timeStamp;
//	private Map<String, UserIdBoundary> createBy;
	private String createBy; 
	private Map<String, Double> location;
	private Map<String, Object> elemntAttributes;

	public ElementEntity() {

	}

	public ElementEntity(String elementId, TypeEnum type, String name, Boolean active, Date timeStamp,
			Map<String, Double> location, Map<String, Object> elemntAttributes, String createBy) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.timeStamp = timeStamp;
		this.createBy = createBy;
		this.location = location;
		this.elemntAttributes = elemntAttributes;
	}

	public Map<String, Double> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Double> location) {
		this.location = location;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Map<String, Object> getElemntAttributes() {
		return elemntAttributes;
	}

	public void setElemntAttributes(Map<String, Object> elemntAttributes) {
		this.elemntAttributes = elemntAttributes;
	}

}
