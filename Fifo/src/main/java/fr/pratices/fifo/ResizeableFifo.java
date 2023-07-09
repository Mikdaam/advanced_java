package fr.pratices.fifo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResizeableFifo<E> extends AbstractQueue<E> {
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

	@Override
	public boolean offer(E element) {
		Objects.requireNonNull(element);
		if (size == capacity) {
			resize();
		}
		elements[tail] = element;
		tail = (tail + 1) % capacity;
		size++;

		return true;
	}

	@Override
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

	@Override
	public E peek() {
		if (isEmpty()) {
			return null;
		}
		return elements[head];
	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	public Iterator<E> iterator() {
		return new Iterator<>() {
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
		return IntStream.iterate(head, i -> (i + 1) % capacity)
				.limit(size)
				.mapToObj(i -> elements[i].toString())
				.collect(Collectors.joining(", ", "[", "]"));
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
