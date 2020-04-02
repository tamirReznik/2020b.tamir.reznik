package acs.element;

/*
{
	"domain":"User",
	"id": 456
}

*/
public class ElementIdBoundary {
	private String domain;
	private int id;

	public ElementIdBoundary() {

	}

	public ElementIdBoundary(String domain, int id) {
		
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
