package fr.uge.set;

public class IntHashSet {
    // * La classe Entry doit avoir les `value` et `next`. Le next est de type entry, ce qui fera une liste chaine automatiquement.
    // * Si l'on doit mettre la classe entry dans le package, ca visibilite doit etre `default`
    // * Les modificateurs des champs doivent etre `default` aussi
    // * Oui, c'est possible
    // * ca permet d'eviter de stocker des nulls dans la liste
    // * si l'on declare `Entry` comme classe interne, on ne pourra pas voir l'implementation de l'exterieur, ce qui est une bonne pratique.
    // * Sa visibilite doit etre `private`
    private record Entry(int value, Entry next) {}
}
