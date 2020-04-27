package acs.data;

import java.util.Date;
import java.util.Map;

import acs.rest.boundaries.element.Location;
import acs.rest.boundaries.user.UserIdBoundary;

public class ElementEntity {
	private String elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private Map<String, UserIdBoundary> createdBy;
	private Location location;
	private Map<String, Object> elemntAttributes;

	public ElementEntity() {

	}

	public ElementEntity(String elementId, String type, String name, Boolean active, Date timeStamp,
			Location location, Map<String, Object> elemntAttributes, Map<String, UserIdBoundary> createBy) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = timeStamp;
		this.createdBy = createBy;
		this.location = location;
		this.elemntAttributes = elemntAttributes;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getElementId() {
		return elementId;
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
		return createdTimestamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.createdTimestamp = timeStamp;
	}

	public Map<String, UserIdBoundary> getCreateBy() {
		return createdBy;
	}

	public void setCreateBy(Map<String, UserIdBoundary> createBy) {
		this.createdBy = createBy;
	}

	public Map<String, Object> getElemntAttributes() {
		return elemntAttributes;
	}

	public void setElemntAttributes(Map<String, Object> elemntAttributes) {
		this.elemntAttributes = elemntAttributes;
	}

}
