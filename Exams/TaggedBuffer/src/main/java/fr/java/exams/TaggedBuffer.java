package fr.java.exams;

import java.util.Arrays;
import java.util.Objects;
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
		if (predicate.test(element)) {
			taggedSize++;
		}
		elements[size] = element;
		size++;
	}

	public int size(boolean onlyTagged) {
		return onlyTagged ? taggedSize : size;
	}
}
