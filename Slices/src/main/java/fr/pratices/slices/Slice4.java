package fr.pratices.slices;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Slice4<T> {
	int size();

	T get(int index);

	Slice4<T> subSlice(int from, int to);

	static <U> Slice4<U> array(U[] array) {
		return array(array, 0, array.length);
	}

	static <U> Slice4<U> array(U[] array, int from, int to) {
		Objects.checkFromToIndex(from, to, array.length);
		return new SliceImpl<>(array, from, to);
	}
}

class SliceImpl<T> implements Slice4<T> {
	private final T[] array;
	private final int from;
	private final int to;
	SliceImpl(T[] array, int from, int to) {
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
	public Slice4<T> subSlice(int from, int to) {
		Objects.checkFromToIndex(from, to, size());
		return new SliceImpl<>(array, this.from + from, this.from + to);
	}

	@Override
	public String toString() {
		return Arrays.stream(array, from, to)
				.map(Objects::toString)
				.collect(Collectors.joining(", ", "[", "]"));
	}
}