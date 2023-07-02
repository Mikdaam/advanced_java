package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice<T> {
	int size();

	T get(int index);

	static <U> Slice<U> array(U[] array) {
		return new ArraySlice<>(array);
	}

	static <U> Slice<U> array(U[] array, int from, int to) {
		Objects.checkFromToIndex(from, to, array.length);
		return new SubArraySlice<>(array, from, to);
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

		@Override
		public String toString() {
			return Arrays.stream(array)
					.map(Objects::toString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
	}

	final class SubArraySlice<T> implements Slice<T> {
		private final T[] array;
		private final int from;
		private final int to;

		SubArraySlice(T[] array, int from, int to) {
			Objects.requireNonNull(array);
			this.array = array;
			this.from = from;
			this.to = to;
		}

		@Override
		public int size() {
			return to - from;
		}

		@Override
		public T get(int index) {
			Objects.checkIndex(index, size());
			return array[from + index];
		}

		@Override
		public String toString() {
			return Arrays.stream(array, from, to)
					.map(Objects::toString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
	}
}
