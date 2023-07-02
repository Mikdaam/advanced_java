package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Slice3<T> {
	int size();

	T get(int index);

	default Slice3<T> subSlice(int from, int to) {
		Objects.checkFromToIndex(from, to, size());
		var slice = this;
		return new Slice3<>() {
			@Override
			public int size() {
				return to - from;
			}

			@Override
			public T get(int index) {
				Objects.checkIndex(index, size());
				return slice.get(from + index);
			}

			@Override
			public String toString() {
				return IntStream.range(0, this.size())
						.boxed()
						.map(this::get)
						.map(Objects::toString)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		};
	}

	static <U> Slice3<U> array(U[] array) {
		Objects.requireNonNull(array);
		return new Slice3<>() {
			@Override
			public int size() {
				return array.length;
			}

			@Override
			public U get(int index) {
				return array[index];
			}

			@Override
			public String toString() {
				return Arrays.stream(array)
						.map(Objects::toString)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		};
	}

	static <U> Slice3<U> array(U[] array, int from, int to) {
		return array(array).subSlice(from, to);
	}
}
