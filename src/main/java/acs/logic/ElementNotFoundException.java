package acs.logic;

public class ElementNotFoundException extends RuntimeException  {
	private static final long serialVersionUID = -3183057250236533850L;

	public ElementNotFoundException() {
		super();
	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementNotFoundException(String message) {
		super(message);
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
	}
}
