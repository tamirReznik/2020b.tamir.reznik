package acs;

public class NameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6508025474230044999L;

	public NameNotFoundException() {
	}

	public NameNotFoundException(String message) {
		super(message);
	}

	public NameNotFoundException(Throwable cause) {
		super(cause);
	}

	public NameNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
