package fr.java.exams;

import java.util.*;
import java.util.function.Consumer;
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

	public void forEach(boolean onlyTagged, Consumer<E> consumer) {
		Objects.requireNonNull(consumer);
		if (onlyTagged) {
			Arrays.stream(elements, 0, size)
					.filter(predicate)
					.forEach(consumer);
		} else {
			Arrays.stream(elements, 0, size).forEach(consumer);
		}
	}

	public Iterator<E> iterator(boolean onlyTagged) {
		if (onlyTagged) {
			return new Iterator<>() {
				private int index = 0;
				private final int itSize = taggedSize;
				@SuppressWarnings("unchecked")
				private final E[] itElements = (E[]) Arrays.stream(elements, 0, size).filter(predicate).toArray();
				@Override
				public boolean hasNext() {
					return index < itSize;
				}

				@Override
				public E next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					var element = itElements[index];
					index++;
					return element;
				}
			};
		}

		return new Iterator<>() {
			private int index = 0;
			@Override
			public boolean hasNext() {
				return index < size;
			}

			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				var element = elements[index];
				index++;
				return element;
			}
		};
	}

	private void resize() {
		capacity *= 2;
		elements = Arrays.copyOf(elements, capacity);
	}
}
