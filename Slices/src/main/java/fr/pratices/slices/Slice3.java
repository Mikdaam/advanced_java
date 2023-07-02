package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Slice3<T> {
	int size();

	T get(int index);

	/*Slice3<T> subSlice(int from, int to);*/

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
}
