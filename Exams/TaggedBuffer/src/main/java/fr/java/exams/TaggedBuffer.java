package fr.java.exams;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TaggedBuffer<E> {
	private class Index extends AbstractList<E> implements RandomAccess {
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
		if (size == 0) { return Optional.empty();}
		Predicate<? super E> finalPredicate = onlyTagged ? predicate : __ -> true;
		return Arrays.stream(elements, 0, size)
				.filter(finalPredicate)
				.findFirst();
	}

	public void forEach(boolean onlyTagged, Consumer<E> consumer) {
		Objects.requireNonNull(consumer);
		Predicate<? super E> finalPredicate = onlyTagged ? predicate : __ -> true;
		Arrays.stream(elements, 0, size).filter(finalPredicate).forEach(consumer);
	}

	public Iterator<E> iterator(boolean onlyTagged) {
		return new Iterator<>() {
			private int index = 0;
			private int position = 0;
			private final E[] copy = elements;
			private final int finalSize = onlyTagged ? taggedSize : size;
			@Override
			public boolean hasNext() {
				return index < finalSize;
			}

			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				E element;

				if (onlyTagged) {
					while (true) {
						element = copy[position++];
						if (predicate.test(element)) {
							index++;
							return element;
						}
					}
				}
				element = copy[position++];
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
	private Spliterator<E> fromArray(boolean onlyTagged, int start, int end, E... array) {
		Predicate<? super E> finalPredicate = onlyTagged ? predicate : __ -> true;
		return new Spliterator<>() {
			private int i = start;
			@Override
			public boolean tryAdvance(Consumer<? super E> action) {
				if (i < end) {
					var element = array[i++];
					if (finalPredicate.test(element)) {
						action.accept(element);
						return true;
					}
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
				var spliterator = fromArray(onlyTagged, i, middle, array);
				i = middle;
				return spliterator;
			}

			@Override
			public long estimateSize() {
				return end - i;
			}

			@Override
			public int characteristics() {
				return ORDERED|NONNULL|(onlyTagged ? 0 : SIZED|SUBSIZED);
			}
		};
	}

	public Stream<E> stream(boolean onlyTagged) {
		var spliterator = fromArray(onlyTagged, 0, size, elements);
		return StreamSupport.stream(spliterator, false);
	}

	private void resize() {
		capacity *= 2;
		elements = Arrays.copyOf(elements, capacity);
	}
}
