package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice2<T> {
	int size();

	T get(int index);

	Slice2<T> subSlice(int from, int to);

	static <U> Slice2<U> array(U[] array) {
		return new ArraySlice<>(array);
	}

	static <U> Slice2<U> array(U[] array, int from, int to) {
		Objects.checkFromToIndex(from, to, array.length);
		return new ArraySlice<U>(array).subSlice(from, to);
	}

	final class ArraySlice<T> implements Slice2<T> {
		final class SubArraySlice implements Slice2<T> {
			private final int from;
			private final int to;
			SubArraySlice(int from, int to) {
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
			public Slice2<T> subSlice(int from, int to) {
				Objects.checkFromToIndex(from, to, size());
				return new SubArraySlice(this.from + from, this.from + to);
			}

			@Override
			public String toString() {
				return Arrays.stream(array, from, to)
						.map(Objects::toString)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		}

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
		public Slice2<T> subSlice(int from, int to) {
			Objects.checkFromToIndex(from, to, size());
			return new SubArraySlice(from, to);
		}

		@Override
		public String toString() {
			return Arrays.stream(array)
					.map(Objects::toString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
	}
}
