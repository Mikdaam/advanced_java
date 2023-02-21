package fr.practices.ymca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public final class House {
    private final ArrayList<People> peoples;
    private final HashMap<Kind, Double> discount;
    public House() {
        peoples = new ArrayList<>();
        discount = new HashMap<>();
    }

    public void add(People people) {
        Objects.requireNonNull(people, "Null forbidden");
        peoples.add(people);
    }

    private double price(People people) {
        Objects.requireNonNull(people, "Can't be null");
        return switch (people) {
            case VillagePeople p -> 100 - (100 * discount.getOrDefault(p.kind(), 0.0));
            case Minion m -> 1;
        };
    }

    public void addDiscount(Kind kind) {
        Objects.requireNonNull(kind, "Can't be null!");
        discount.put(kind, 0.8);
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