package fr.practices.ymca;

import java.util.Objects;

public record VillagePeople(String name, Kind kind) implements People {
    public VillagePeople {
        Objects.requireNonNull(name, "Not null");
        Objects.requireNonNull(kind, "Not null");
    }

    @Override
    public String toString() {
        return name + " (" + kind + ")";
    }
}