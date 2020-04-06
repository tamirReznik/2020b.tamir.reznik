package acs.data;

import java.sql.Date;
import java.util.Map;

import acs.TypeEnum;
import acs.rest.boundaries.ElementIdBoundary;
import acs.rest.boundaries.Location;

public class ElementEntity {
	private ElementIdBoundary elementId;
	private TypeEnum type;
	private String name;
	private Boolean active;
	private Date timeStamp;
	private Map<String, Object> createBy;
	private Location location;
	private Map<String, Object> elemntAttributes;
	
	public ElementEntity () {
		
	}
	
	public ElementEntity(ElementIdBoundary elementId, TypeEnum type, String name, Boolean active, Date timeStamp,
			Location location, Map<String, Object> elemntAttributes, Map<String, Object> createBy) {
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

	public ElementIdBoundary getElementId() {
		return elementId;
	}

	public void setElementId(ElementIdBoundary elementId) {
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

	public Map<String, Object> getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Map<String, Object> createBy) {
		this.createBy = createBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Map<String, Object> getElemntAttributes() {
		return elemntAttributes;
	}

	public void setElemntAttributes(Map<String, Object> elemntAttributes) {
		this.elemntAttributes = elemntAttributes;
	}
	
	
}
