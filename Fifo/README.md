# Fifo Java Lab

Exercise 1 - Maven Configuration
---

Exercise 2 - Fifo
---
On souhaite écrire une structure de données pour représenter un file d'attente (first-in first-out ou FIFO) utilisant un tableau circulaire qui aura, au moins au début du TP, une taille fixée à la création.
Les deux opérations principales sont offer qui permet d'insérer un élément à la fin de la file et poll qui permet de retirer un élément en début de la file.
L'idée est d'utiliser deux indices, un correspondant à la tête de la file pour retirer les éléments et un correspondant à la queue de la file pour ajouter des éléments. Lorsque l'on insérera un élément, on incrémentera la queue de la file, lorsque l'on retirera un élément, on incrémentera la tête de la file.
De plus, il est impossible de stocker null dans la file.

1. Cette représentation peut poser problème, car si la tête et la queue correspondent au même indice, il n'est pas facile de détecter si cela veux dire que la file est pleine ou vide.
Comment doit-on faire pour détecter si la file est pleine ou vide ?
Cette question a plusieurs réponses possibles :).
> La file est pleine lorsque l'indice de la tete > l'indice de la queu

2. Écrire une classe Fifo générique (avec une variable de type E) dans le package fr.uge.fifo prenant en paramètre le nombre maximal d’éléments que peut stocker la structure de données. Pensez à vérifier les préconditions.

3. Écrire la méthode offer qui ajoute un élément de type E dans la file. Pensez à vérifier les préconditions sachant que, notamment, on veut interdire le stockage de null.
Comment détecter que la file est pleine ?
Que faire si la file est pleine ?

4. Écrire une méthode poll qui retire un élément de type E de la file. Penser à vérifier les préconditions.
Que faire si la file est vide ?

5. Ajouter une méthode d'affichage qui affiche les éléments dans l'ordre dans lequel ils seraient sortis en utilisant poll. L'ensemble des éléments devra être affiché entre crochets ('[' et ']') avec les éléments séparés par des virgules (suivies d'un espace).
Note : attention à bien faire la différence entre la file pleine et la file vide.
Note 2 : Il existe une classe StringJoiner qui est ici plus pratique à utiliser qu'un StringBuilder !
Indication : Vous avez le droit d'utiliser 2 compteurs.

6. Rappelez ce qu'est un memory leak en Java et assurez-vous que votre implantation n'a pas ce comportement indésirable.

> Un `memory leak` est la fuite de donnée dans le programme. <br>
> Par exemple, le fait d'oublier de desalouer un objet.
7. Ajouter une méthode size et une méthode isEmpty.

8. Rappelez quel est le principe d'un itérateur.
Quel doit être le type de retour de la méthode iterator() ?

9. Implanter la méthode iterator().
Note : ici, pour simplifier le problème, on considérera que l'itérateur ne peut pas supprimer des éléments pendant son parcours.

10. Rappeler à quoi sert l'interface Iterable.
Faire en sorte que votre file soit Iterable.
Vérifier avec le test unitaire suivant que votre implantation est bien conforme : FifoTest.java.