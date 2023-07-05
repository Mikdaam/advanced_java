package fr.uge.set;

import java.util.Objects;
import java.util.function.IntConsumer;

public class IntHashSet {
    // * 1 -
    // * La classe Entry doit avoir les `value` et `next`. Le next est de type entry, ce qui fera une liste chaine automatiquement.
    // * Si l'on doit mettre la classe entry dans le package, ca visibilite doit etre `default`
    // * Les modificateurs des champs doivent etre `default` aussi
    // * Oui, c'est possible
    // * ca permet d'eviter de stocker des nulls dans la liste
    // * si l'on declare `Entry` comme classe interne, on ne pourra pas voir l'implementation de l'exterieur, ce qui est une bonne pratique.
    // * Sa visibilite doit etre `private`
    private record Entry(int value, Entry next) {}

    private final int capacity = 2;
    private final Entry[] entries = new Entry[capacity];
    private int size = 0;

    public IntHashSet() {

    }

    public void add(int value) {
        if (isPresent(value)) return;
        int hash = hash(value);
        var head = entries[hash];
        entries[hash] = new Entry(value, head);
        size++;
    }

    public int size() {
        return size;
    }


    // * 4 -
    // * la signature de la `functional interface` est (T) -> void
    // * le nom de la classe ayant la meme signature est `Consumer<T>`
    public void forEach(IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        for (var entry : entries) {
            var current = entry;
            while (current != null) {
                consumer.accept(current.value);
                current = current.next();
            }
        }
    }

    private boolean isPresent(int value) {
        for (var entry : entries) {
            var current = entry;
            while (current != null) {
                if (current.value == value) return true;
                current = current.next();
            }
        }

        return false;
    }

    private int hash(int value) {
        return value & (capacity - 1);
    }
}
