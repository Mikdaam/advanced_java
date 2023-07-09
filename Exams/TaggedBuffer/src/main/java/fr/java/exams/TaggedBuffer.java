package fr.java.exams;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class TaggedBuffer<E> {
	private final Predicate<? super E> predicate;
	private E[] elements;
	private int capacity = 4;
	private int size, taggedSize;
	@SuppressWarnings("unchecked")
	public TaggedBuffer(Predicate<? super E> predicate) {
		Objects.requireNonNull(predicate);
		this.predicate = predicate;
		this.elements = (E[]) new Object[capacity];
	}

	public void add(E element) {
		Objects.requireNonNull(element);
		if (size == capacity) {
			resize();
		}
		if (predicate.test(element)) {
			taggedSize++;
		}
		elements[size] = element;
		size++;
	}

	public int size(boolean onlyTagged) {
		return onlyTagged ? taggedSize : size;
	}

	public Optional<E> findFirst(boolean onlyTagged) {
		if (size == 0) {
			return Optional.empty();
		}

		if (onlyTagged) {
			return Arrays.stream(elements)
					.filter(predicate)
					.findFirst();
		}
		return Arrays.stream(elements).findFirst();
		/*return Arrays.stream(elements, 0, size)
				.filter(e -> onlyTagged || predicate.test(e))
				.findFirst();*/
	}

	private void resize() {
		capacity *= 2;
		elements = Arrays.copyOf(elements, capacity);
	}
}
