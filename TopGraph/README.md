# Top Graph Java Lab

## Subject
Le but de ce TD est d'implanter diverses représentations des graphes orientés et de "jouer" avec les types paramétrés, les lambdas, les itérateurs et les streams.
Le TD a pour but de fournir deux implantations différentes de l'interface Graph.java. Il s'agit de graphes orientés qui ont un nombre de nœuds fixe (numérotés de 0 à nodeCount - 1). <br>
Les arcs sont valués (par des objets, dont le type est type est indiqué par T). Les nœuds ne contiennent pas d'information.
> - Le constructeur de chaque implantation prend le nombre de nœuds du graphe en paramètre.
> - La méthode `addEdge(src, dst, weight)` ajoute un arc avec un poids ou remplace le poids de l'arc s'il existait avant.
> - La méthode `getWeight(src, dst)` renvoie le poids de l'arc entre src et dst. On utilisera un Optional pour modéliser l'absence possible d'arc.
> - La méthode `edges(src, consumer)` qui prend un nœud en paramètre et appelle le consumer avec chaque arc qui a ce nœud comme source.

Vous pouvez aussi noter que l'interface `Graph` est correctement documentée en utilisant le format javadoc. Non seulement on indique pour chaque méthode publique ce qu'elle fait, à quoi correspond chaque paramètre, et la valeur de retour attendue, mais on documente également les exceptions susceptibles d'être levées.

## Exercice 1 - Maven Config

## Exercice 2 - MatrixGraph
La classe MatrixGraph, dans le package fr.uge.graph, est une implantation par matrice d'adjacence de l'interface Graph. La structure de données est une matrice nodeCount * nodeCount telle que l'on stocke le poids d'un arc (src, dst) dans la case (src, dst).
En fait, habituellement, on ne représente pas une matrice sous forme d'un tableau à double entrée, car cela veut dire effectuer deux indirections pour trouver la valeur. On alloue un tableau à une seule dimension de taille nodeCount * nodeCount et on se balade dedans en faisant des additions et des multiplications.

Les tests unitaires qui vérifient que votre implantation est bien conforme sont là : GraphTest.java

1. Indiquer comment trouver la case (i, j) dans un tableau à une seule dimension de taille nodeCount * nodeCount.
Si vous n'y arrivez pas, faites un dessin !

2. Rappeler pourquoi, en Java, il n'est pas possible de créer des tableaux de variables de type puis implanter la classe MatrixGraph et son constructeur.
Pouvez-vous supprimer le warning à la construction ? Pourquoi?
Vérifier que les tests marqués "Q2" passent.

3. On peut remarquer que la classe MatrixGraph n'apporte pas de nouvelles méthodes par rapport aux méthodes de l'interface Graph donc il n'est pas nécessaire que la classe MatrixGraph soit publique.
Ajouter une méthode factory nommée createMatrixGraph dans l'interface Graph et déclarer la classe MatrixGraph non publique.
Vérifier que les tests marqués "Q3" passent.

4. Afin d'implanter correctement la méthode getWeight, rappeler à quoi sert la classe java.util.Optional en Java.
Implanter la méthode addEdge sachant que l'on ne peut pas créer un arc sans valeur.
Implanter la méthode getWeight.
Vérifier que les tests marqués "Q4" passent.

5. Implanter la méthode edges puis vérifier que les tests marqués "Q5" passent.

6. Rappeler le fonctionnement d'un itérateur et de ses méthodes hasNext et next.
Que renvoie next si hasNext retourne false ?
Expliquer pourquoi il n'est pas nécessaire, dans un premier temps, d'implanter la méthode remove qui fait pourtant partie de l'interface.
Implanter la méthode neighborsIterator(src) qui renvoie un itérateur sur tous les nœuds ayant un arc dont la source est src.
Vérifier que les tests marqués "Q6" passent.
Note : ça pourrait être une bonne idée de calculer quel est le prochain arc valide AVANT que l'on vous demande s'il existe.

7. Pourquoi le champ nodeCount ne doit pas être déclaré private avant Java 11 ?
Est-ce qu'il y a d'autres champs qui ne doivent pas être déclarés private avant Java 11 ?

8. On souhaite écrire la méthode neighborStream(src) qui renvoie un IntStream contenant tous les nœuds ayant un arc sortant par src.
Pour créer le Stream, nous allons utiliser StreamSupport.intStream qui prend en paramètre un Spliterator.OfInt. Rappeler ce qu'est un Spliterator, à quoi sert le OfInt et quelles sont les méthodes qu'il va falloir redéfinir.
Écrire la méthode neighborStream sachant que l'on implantera le Spliterator en utilisant l'itérateur défini précédemment.
Vérifier que les tests marqués "Q8" passent.

9. On peut remarquer que neighborStream dépend de neighborsIterator et donc pas d'une implantation spécifique. On peut donc écrire neighborStream directement dans l'interface Graph comme ça le code sera partagé.
Rappeler comment on fait pour avoir une méthode 'instance avec du code dans une interface.
Déplacer neighborStream dans Graph et vérifier que les tests unitaires passent toujours.

10. Expliquer le fonctionnement précis de la méthode remove de l'interface Iterator.
Implanter la méthode remove de l'itérateur.
Vérifier que les tests marqués "Q10" passent.

11. On peut remarquer que l'on peut ré-écrire edges en utilisant neighborsStream, en une ligne :) et donc déplacer edges dans Graph.
Déplacer le code de la méthode edges dans Graph.