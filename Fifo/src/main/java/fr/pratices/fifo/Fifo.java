package fr.pratices.fifo;

import java.util.Objects;
import java.util.StringJoiner;

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
		elements[head] = null;
		head = (head + 1) % maxElements;
		size--;
		return element;
	}

	@Override
	public String toString() {
		var joiner = new StringJoiner(", ", "[", "]");
		int count = head;
		for (int i = 0; i < size; i++) {
			joiner.add(elements[count].toString());
			count = (count + 1) % maxElements;
		}

		return joiner.toString();
	}
}
