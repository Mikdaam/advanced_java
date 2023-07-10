package fr.exams.entropyset;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntropySet<T> extends AbstractSet<T> {
	private static final int CAPACITY = 4;

	private final LinkedHashSet<T> set = new LinkedHashSet<>();
	@SuppressWarnings("unchecked")
	private final T[] cache = (T[]) new Object[CAPACITY];
	private boolean frozen;

	public boolean add(T element) {
		Objects.requireNonNull(element);
		if (frozen) {
			throw new UnsupportedOperationException();
		}
		for (int i = 0; i < cache.length; i++) {
			if (cache[i] == null) {
				cache[i] = element;
				return false;
			}
			if (cache[i].equals(element)) {
				return false;
			}
		}
		set.add(element);
		return false;
	}

	public int size() {
		frozen = true;
		for (int i = 0; i < cache.length; i++) {
			if (cache[i] == null) {
				return i;
			}
		}
		return CAPACITY + set.size();
	}

	public boolean contains(Object element) {
		Objects.requireNonNull(element);
		frozen = true;
		for (var elt : cache) {
			if (elt == null) {
				return false;
			}
			if (element.equals(elt)) {
				return true;
			}
		}
		return set.contains(element);
	}

	public boolean isFrozen() {
		return frozen;
	}

	public Iterator<T> iterator() {
		frozen = true;
		return new Iterator<T>() {
			private int i = 0;
			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				if (i < CAPACITY) {
					return cache[i++];
				}
				@SuppressWarnings("unchecked")
				var arrayElt = (T[]) set.toArray();
				var element = arrayElt[i - CAPACITY];
				i++;
				return element;
			}
		};
	}

	private Spliterator<T> fromIterator(Iterator<T> it) {
		return new Spliterator<T>() {
			@Override
			public boolean tryAdvance(Consumer<? super T> action) {
				if (it.hasNext()) {
					action.accept(it.next());
					return true;
				}
				return false;
			}

			@Override
			public Spliterator<T> trySplit() {
				return null;
			}

			@Override
			public long estimateSize() {
				return Long.MAX_VALUE;
			}

			@Override
			public int characteristics() {
				return DISTINCT | ORDERED | NONNULL;
			}
		};
	}

	public Stream<T> stream() {
		return StreamSupport.stream(fromIterator(iterator()), true);
	}

	public static <T> EntropySet<T> from(Collection<? extends T> elements) {
		Objects.requireNonNull(elements);
		var newSet = new EntropySet<T>();
		if (elements.spliterator().hasCharacteristics(Spliterator.DISTINCT|Spliterator.NONNULL)) {
			newSet.addAll(elements);
		} else {
			var t = Set.copyOf(elements);
			newSet.addAll(t);
		}

		return newSet;
	}
}
