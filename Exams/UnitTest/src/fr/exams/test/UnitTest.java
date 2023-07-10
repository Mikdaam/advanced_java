package fr.exams.test;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UnitTest {
	private final HashMap<String, Runnable> tests = new HashMap<>();

	public void test(String name, Runnable run) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(run);
		if (tests.containsKey(name)) {
			throw new IllegalStateException();
		}
		tests.put(name, run);
	}

	public int testCount() {
		return tests.size();
	}

	public List<Error> runOnly(String testName) {
		Objects.requireNonNull(testName);
		var action = tests.getOrDefault(testName, null);
		if (action == null) {
			throw new IllegalStateException();
		}
		try {
			action.run();
		} catch (Exception exception) {
			return List.of(new AssertionError(exception));
		} catch (Error e) {
			return List.of(e);
		}

		return List.of();
	}

	public static void checkEquals(Object value1, Object value2) {
		if (!Objects.equals(value1, value2)) {
			throw new AssertionError(value1 + " is not equal to " + value2);
		}
	}
}
