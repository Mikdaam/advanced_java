package fr.practices.ymca;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public final class House {
    private final ArrayList<People> peoples;
    public House() {
        peoples = new ArrayList<>();
    }

    public void add(People people) {
        Objects.requireNonNull(people, "Null forbidden");
        peoples.add(people);
    }

    private double price(People people) {
        return switch (people) {
            case VillagePeople p -> 100;
            case Minion m -> 1;
        };
    }

    public double averagePrice() {
        return peoples.stream().mapToDouble(this::price).average().orElse(Double.NaN);
    }

    @Override
    public String toString() {
        if (peoples.isEmpty()) {
            return "Empty House";
        }

        return "House with " + peoples.stream()
                .map(People::name)
                .sorted()
                .collect(Collectors.joining(", "));
    }
}