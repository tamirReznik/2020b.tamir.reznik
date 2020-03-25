package demo;

import java.sql.Date;
import java.util.Map;

public class ElementBoundary {
	ElementIdBoundary elementId;
	TypeEnum type;
	String name;
	Boolean active;
	Date timeStamp;
	private Location location;
	private Map<String, Object> elemntAttributes;

	public ElementBoundary(ElementIdBoundary elementId, TypeEnum type, String name, Boolean active, Date timeStamp,
			Location location, Map<String, Object> elemntAttributes) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.timeStamp = timeStamp;
		this.location = location;
		this.elemntAttributes = elemntAttributes;
	}

	public ElementBoundary() {
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
