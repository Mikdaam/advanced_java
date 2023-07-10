package fr.exams.entropyset;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;

public class EntropySet<T> {
	private static final int CAPACITY = 4;

	private final LinkedHashSet<T> set = new LinkedHashSet<>();
	@SuppressWarnings("unchecked")
	private final T[] cache = (T[]) new Object[CAPACITY];
	private boolean frozen;

	public void add(T element) {
		Objects.requireNonNull(element);
		if (frozen) {
			throw new UnsupportedOperationException();
		}
		for (int i = 0; i < cache.length; i++) {
			if (cache[i] == null) {
				cache[i] = element;
				return;
			}
			if (cache[i].equals(element)) {
				return;
			}
		}
		set.add(element);
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
}
