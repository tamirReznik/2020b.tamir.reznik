package demo;

/*
	{
		"first":"User",
		"last":"Demo"
	}
 
 */
public class NameBoundary {
	private String first;
	private String last;

	public NameBoundary() {
	}

	public NameBoundary(String first, String last) {
		super();
		this.first = first;
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return "NameBoundary [first=" + first + ", last=" + last + "]";
	}
}
