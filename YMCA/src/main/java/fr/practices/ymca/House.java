package fr.practices.ymca;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class House {
    private final ArrayList<VillagePeople> peoples;
    public House() {
        peoples = new ArrayList<>();
    }

    public void add(VillagePeople people) {
        Objects.requireNonNull(people, "Null forbidden");
        peoples.add(people);
    }

    @Override
    public String toString() {
        if (peoples.isEmpty()) {
            return "Empty House";
        }

        return "House with " + peoples.stream()
                .map(VillagePeople::name)
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
