package fr.pratices.slices;

import java.util.Objects;

public sealed interface Slice<T> {
	int size();

	T get(int index);

	static <U> Slice<U> array(U[] array) {
		return new ArraySlice<>(array);
	}

	final class ArraySlice<T> implements Slice<T> {
		private final T[] array;

		 ArraySlice(T[] array) {
			Objects.requireNonNull(array);
			this.array = array;
		}
		@Override
		public int size() {
			return array.length;
		}

		@Override
		public T get(int index) {
			return array[index];
		}
	}
}
