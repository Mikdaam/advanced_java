package fr.practices.ymca;

import java.util.ArrayList;
import java.util.Objects;

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

        var separator = "";
        var peoplesString = new StringBuilder();
        peoplesString.append("House with ");
        for (var people : peoples) {
            peoplesString.append(separator).append(people.name());
            separator = ", ";
        }
        return peoplesString.toString();
    }
}
