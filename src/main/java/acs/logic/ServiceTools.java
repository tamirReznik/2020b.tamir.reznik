package acs.logic;

public class ServiceTools {

	static public void stringValidation(String... strings) {
		for (String string : strings)
			if (string == null || string.trim().isEmpty())
				throw new RuntimeException("Any Url String Variable Must Not Be Empty Or null");

	}

	static public void validatePaging(int size, int page) {
		if (size < 1)
			throw new RuntimeException("size must be not less than 1");

		if (page < 0)
			throw new RuntimeException("page must not be negative");

	}
}
