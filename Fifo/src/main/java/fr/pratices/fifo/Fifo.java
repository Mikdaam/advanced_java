package fr.pratices.fifo;

import java.util.Objects;

public class Fifo<E> {
	private final E[] elements;
	private final int maxElements;
	private int head, tail, size;

	@SuppressWarnings("unchecked")
	public Fifo(int maxElements) {
		if (maxElements <= 0) {
			throw new IllegalArgumentException();
		}
		this.maxElements = maxElements;
		elements = (E[]) new Object[maxElements];
	}

	public void offer(E element) {
		Objects.requireNonNull(element);
		if (size == maxElements) {
			throw new IllegalStateException();
		}
		elements[tail] = element;
		tail = (tail + 1) % maxElements;
		size++;
	}

	public E poll() {
		if (size == 0) {
			throw new IllegalStateException();
		}
		var element = elements[head];
		head = (head + 1) % maxElements;
		size--;
		return element;
	}
}
