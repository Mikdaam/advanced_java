package fr.exams.test;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class UnitTest {
	interface Ensure<T> {
		void equalsTo(T value);
		Ensure<T> not();
	}
	interface Return<T> {
		Ensure<T> returnValue();
		void throwsAnException(Class<?> exceptionClass);
	}

	private static class EnsureImpl<T> implements Ensure<T> {
		private final Ensure<T> parent = this;
		private final T value;

		private EnsureImpl(T value) {
			this.value = value;
		}

		@Override
		public void equalsTo(T otherValue) {
			checkEquals(value, otherValue);
		}

		@Override
		public Ensure<T> not() {
			return new Ensure<>() {
				@Override
				public void equalsTo(T otherValue) {
					if (Objects.equals(value, otherValue)) {
						throw new AssertionError(value + " is equal to " + otherValue);
					}
				}

				@Override
				public Ensure<T> not() {
					return parent;
				}
			};
		}
	}
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

	public static <T> Ensure<T> ensure(T firstValue) {
		return new EnsureImpl<>(firstValue);
	}

	public static void checkEquals(Object value1, Object value2) {
		if (!Objects.equals(value1, value2)) {
			throw new AssertionError(value1 + " is not equal to " + value2);
		}
	}

	public static <T> Return<T> ensureCode(Supplier<T> supplier) {
		Objects.requireNonNull(supplier);
		T res = null;
		Class<?> exceptClass = null;
		boolean exceptionThrown;
		try {
			res = supplier.get();
			exceptionThrown = false;
		} catch (Exception e) {
			exceptionThrown = true;
			exceptClass = e.getClass();
		}
		boolean finalExceptionThrown = exceptionThrown;
		Class<?> finalExceptClass = exceptClass;
		T finalRes = res;
		return new Return<T>() {
			@Override
			public Ensure<T> returnValue() {
				return new EnsureImpl<>(finalRes);
			}

			@Override
			public void throwsAnException(Class<?> exceptionClass) {
				if (!finalExceptionThrown) {
					throw new AssertionError("expect " + exceptionClass.getName() + " but no exception was thrown");
				}

				if (!finalExceptClass.equals(exceptionClass)) {
					throw new AssertionError("unexpected exception " + finalExceptClass.getName());
				}
			}
		};
	}
}
