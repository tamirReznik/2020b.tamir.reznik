package demo;

import java.util.Date;
import java.util.Map;

/*
{
	"message":"hello",
	"timestamp":"20200323T103001",
	"important":true,
	"value":99.99,
	"type":"CRITICAL",
	"details":{
	
	},
	"name":{
		"first":"User",
		"last":"Demo"
	}
} 
 */
public class ComplexMessagBoundary {
	private String message;
	private Date timestamp;
	private Boolean important;
	private Double value;
	private TypeEnum type;
	private Map<String, Object> details;
	private NameBoundary name;
	
	public ComplexMessagBoundary() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	public NameBoundary getName() {
		return name;
	}

	public void setName(NameBoundary name) {
		this.name = name;
	}
	
	
	
}
