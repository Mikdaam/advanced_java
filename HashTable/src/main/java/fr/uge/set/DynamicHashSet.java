package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;

public class DynamicHashSet<T> {
    private record Entry<T>(T value, Entry<T> next) {}

    private final int capacity = 2;
    @SuppressWarnings("unchecked")
    private final Entry<T>[] entries = (Entry<T>[]) new Entry<?>[capacity];
    private int size = 0;

    public void add(T value) {
        Objects.requireNonNull(value);
        if (isPresent(value)) return;
        int hash = hash(value);
        var head = entries[hash];
        entries[hash] = new Entry<>(value, head);
        size++;
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        for (var entry : entries) {
            var current = entry;
            while (current != null) {
                consumer.accept(current.value);
                current = current.next();
            }
        }
    }

    public boolean contains(T value) {
        Objects.requireNonNull(value);
        return isPresent(value);
    }

    private boolean isPresent(T value) {
        for (var entry : entries) {
            var current = entry;
            while (current != null) {
                if (current.value.equals(value)) return true;
                current = current.next();
            }
        }

        return false;
    }

    private int hash(T value) {
        return value.hashCode() & (capacity - 1);
    }
}
