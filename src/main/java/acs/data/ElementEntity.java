package acs.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.dal.MapToJsonConverter;
import acs.rest.boundaries.element.Location;
import acs.rest.boundaries.user.UserIdBoundary;

@Entity
@Table(name = "ELEMENTS")
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

	public ElementEntity(String elementId, String type, String name, Boolean active, Date timeStamp, Location location,
			Map<String, Object> elemntAttributes, Map<String, UserIdBoundary> createBy) {
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

	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Id
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimeStamp() {
		return createdTimestamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.createdTimestamp = timeStamp;
	}

	@Convert(converter = MapToJsonConverter.class)
	@Lob
	public Map<String, UserIdBoundary> getCreateBy() {
		return createdBy;
	}

	public void setCreateBy(Map<String, UserIdBoundary> createBy) {
		this.createdBy = createBy;
	}

	@Convert(converter = MapToJsonConverter.class)
	@Lob
	public Map<String, Object> getElemntAttributes() {
		return elemntAttributes;
	}

	public void setElemntAttributes(Map<String, Object> elemntAttributes) {
		this.elemntAttributes = elemntAttributes;
	}

}
