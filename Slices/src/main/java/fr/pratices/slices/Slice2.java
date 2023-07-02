package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice2<T> {
	int size();

	T get(int index);

	/* Slice2<T> subSlice(int from, int to); */

	static <U> Slice2<U> array(U[] array) {
		return new ArraySlice<>(array);
	}

	final class ArraySlice<T> implements Slice2<T> {
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

		/*@Override
		public Slice2<T> subSlice(int from, int to) {
			Objects.checkFromToIndex(from, to, size());
			return new SubArraySlice<>(array, from, to);
		}*/

		@Override
		public String toString() {
			return Arrays.stream(array)
					.map(Objects::toString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
	}
}
