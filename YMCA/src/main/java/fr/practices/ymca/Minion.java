package fr.practices.ymca;

import java.util.Objects;

public record Minion(String name) implements People {
    public Minion {
        Objects.requireNonNull(name, "Not null");
    }

    @Override
    public String toString() {
        return name + " (MINION)";
    }
}
