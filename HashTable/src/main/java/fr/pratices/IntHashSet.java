package fr.pratices;

public class IntHashSet {
    static final class Entry {
        private final int value;
        private final Entry next;

        Entry(int value, Entry next) {
            this.value = value;
            this.next = next;
        }
    }
}
