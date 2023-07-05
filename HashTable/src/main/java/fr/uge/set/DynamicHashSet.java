package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;

public class DynamicHashSet<T> {
    private record Entry<T>(T value, Entry<T> next) {}

    private int capacity = 2048;
    @SuppressWarnings("unchecked")
    private Entry<T>[] entries = (Entry<T>[]) new Entry<?>[capacity];
    private int size = 0;

    @SuppressWarnings("unchecked")
    public void add(T value) {
        Objects.requireNonNull(value);
        if (isPresent(value)) return;
        int hash = hash(value);
        var head = entries[hash];
        entries[hash] = new Entry<>(value, head);
        size++;

        if (capacity < (size / 2)) {
            capacity *= 2;
            size = 0;
            var newEntries = (Entry<T>[]) new Entry<?>[capacity];
            for (var entry : entries) {
                var current = entry;
                while (current != null) {
                    int newHash = hash(current.value);
                    var newHead = newEntries[newHash];
                    newEntries[newHash] = new Entry<>(current.value, newHead);
                    size++;
                    current = current.next();
                }
            }
            entries = newEntries;
        }
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

    public boolean contains(Object value) {
        Objects.requireNonNull(value);
        return isPresent(value);
    }

    private boolean isPresent(Object value) {
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
