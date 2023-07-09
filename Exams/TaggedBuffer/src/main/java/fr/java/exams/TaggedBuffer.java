package fr.java.exams;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TaggedBuffer<E> {
	class Index extends AbstractList<E> implements RandomAccess {
		private final int[] indexes;
		private Index(int[] indexes) {
			Objects.requireNonNull(indexes);
			this.indexes = indexes;
		}

		@Override
		public E get(int index) {
			Objects.checkIndex(index, indexes.length);
			return elements[indexes[index]];
		}

		@Override
		public int size() {
			return indexes.length;
		}
	}
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
				.filter(e -> onlyTagged && predicate.test(e))
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

	public List<E> asTaggedList() {
		int[] indexes = new int[taggedSize];
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (predicate.test(elements[i])) {
				indexes[j] = i;
				j++;
			}
		}
		return new Index(indexes);
	}

	@SafeVarargs
	private Spliterator<E> fromArray(int start, int end, E... array) {
		return new Spliterator<>() {
			private int i = start;
			@Override
			public boolean tryAdvance(Consumer<? super E> action) {
				if (i < end) {
					action.accept(array[i++]);
					return true;
				}
				return false;
			}

			@Override
			public Spliterator<E> trySplit() {
				var middle = (i + end) >>> 1;
				if (i == middle) {
					return null;
				}
				var spliterator = fromArray(i, middle, array);
				i = middle;
				return spliterator;
			}

			@Override
			public long estimateSize() {
				return end - i;
			}

			@Override
			public int characteristics() {
				return SIZED|SUBSIZED;
			}
		};
	}

	public Stream<E> stream(boolean onlyTagged) {
		var spliterator = fromArray(0, size, elements);
		return StreamSupport.stream(spliterator, false);
	}

	private void resize() {
		capacity *= 2;
		elements = Arrays.copyOf(elements, capacity);
	}
}
