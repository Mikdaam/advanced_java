package fr.pratices.fifo;

import java.util.*;

public class ResizeableFifo<E> implements Iterable<E> {
	private E[] elements;
	private int capacity;
	private int head, tail, size;

	@SuppressWarnings("unchecked")
	public ResizeableFifo(int initialCapacity) {
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.capacity = initialCapacity;
		elements = (E[]) new Object[initialCapacity];
	}

	public void offer(E element) {
		Objects.requireNonNull(element);
		if (size == capacity) {
			resize();
		}
		elements[tail] = element;
		tail = (tail + 1) % capacity;
		size++;
	}

	public E poll() {
		if (size == 0) {
			return null;
		}
		var element = elements[head];
		elements[head] = null;
		head = (head + 1) % capacity;
		size--;
		return element;
	}

	public int size() { return size; }

	public boolean isEmpty() { return size == 0; }

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int cursor = head;
			private int i = 0;
			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				var element = elements[cursor];
				cursor = (cursor + 1) % capacity;
				i++;
				return element;
			}
		};
	}

	@Override
	public String toString() {
		var joiner = new StringJoiner(", ", "[", "]");
		int count = head;
		for (int i = 0; i < size; i++) {
			joiner.add(elements[count].toString());
			count = (count + 1) % capacity;
		}

		return joiner.toString();
	}

	private void resize() {
		capacity *= 2;
		if (head == tail) {
			@SuppressWarnings("unchecked")
			var array = (E[]) new Object[capacity];
			System.arraycopy(elements, head, array, 0, (size - head));
			System.arraycopy(elements, 0, array, (size - tail), tail);
			elements = array;
			tail = size;
			head = 0;
		} else {
			elements = Arrays.copyOf(elements, capacity);
		}
	}
}
