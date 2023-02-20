package fr.practices.ymca;

public sealed interface People permits VillagePeople, Minion {
    String name();
}
