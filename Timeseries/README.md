# TimeSeries Java Lab

Exercice 2 - TimeSeries
---

Le but de cet exercice est d'implanter une structure de données dite time-series. Une structure de données time-series est une liste de valeurs. Chaque valeur possède aussi un marqueur temporel (un timestamp) associé.
Ça permet, par exemple, de stocker les valeurs d'un capteur de température d'une pièce pendant la journée.
Voici un exemple d'utilisation
```java
var timeSeries = new TimeSeries<String>();
		timeSeries.add(23L, "hello");
		timeSeries.add(34L, "time");
		timeSeries.add(70L, "serie");
```

On créé une instance de TimeSeries paramétrée par le type d'élément que l'on veut y ajouter, puis on ajoute les éléments précédés de leur timestamp.
Les timestamps doivent être croissants : leur valeur est toujours supérieure ou égale à la valeur du timestamp précédent.
Les éléments insérés ne peuvent pas être null.
Par ailleurs, on peut créer des index (de type `Index`) qui représentent uniquement certaines valeurs à l'intérieur de la structure de données `TimeSeries`. <br>
Un `Index` est un tableau d'entiers rangés en ordre croissant, contenant les indices des éléments dans la TimeSeries.
Par exemple, avec la TimeSeries créé précédemment, un Index contenant les indices `[0, 2]` correspond aux éléments "hello" et "serie".
Il y a deux façons de créer des `Index`. On peut soit demander de créer un Index contenant tous les indices, avec la méthode index(); soit créer un Index contenant les indices des éléments pour lesquels une lambda prise en paramètre est vraie, avec la méthode index(filter)
Voici un exemple de création et d'utilisation des index.
```java
var timeSeries = new TimeSeries<String>();
		...
		var index = timeSeries.index(s -> s.startWith("t"));
		System.out.println(index.size()); // 1 (il n'y a que "time" qui renvoie vrai pour le filtre)
```


Les tests JUnit sont ici TimeSeriesTest.java.

1. Dans un premier temps, on va créer une classe TimeSeries ainsi qu'un record Data à l'intérieur de la classe TimeSeries qui représente une paire contenant une valeur de temps (timestamp) et un élément (element).
Le record Data est paramétré par le type de l'élément qu'il contient.
Écrire la classe TimeSeries dans le package fr.uge.serie, ainsi que le record interne public Data et vérifier que les tests marqués "Q1" passent.

2. On souhaite maintenant écrire les méthodes dans TimeSeries :
add(timestamp, element) qui permet d'ajouter un élément avec son timestamp.
La valeur de timestamp doit toujours être supérieure ou égale à la valeur du timestamp précédemment inséré (s'il existe).
size qui renvoie le nombre d'éléments ajoutés.
get(index) qui renvoie l'objet Data se trouvant à la position indiquée par l'index (de 0 à size - 1).

> En interne, la classe TimeSeries stocke des instances de Data dans une liste qui s'agrandit dynamiquement.
Écrire les 3 méthodes définies ci-dessus et vérifier que les tests marqués "Q2" passent.

3. On souhaite maintenant créer une classe interne publique Index ainsi qu'une méthode index permettant de créer un Index stockant les indices des données de la TimeSeries sur laquelle la méthode index est appelée. L'objectif est de pouvoir ensuite accéder aux Data correspondantes dans le TimeSeries. Un Index possède une méthode size indiquant combien d'indices il contient.
Seuls les indices des éléments ajoutés avant l'appel à la méthode index() doivent être présents dans l'Index.
En interne, un Index stocke un tableau d'entiers correspondants à chaque indice.
Écrire la méthode index et vérifier que les tests marqués "Q3" passent.
Indication : Instream.range() permet de créer un Stream d'entiers.

4. On souhaite pouvoir afficher un Index, c'est à dire afficher les éléments (avec le timestamp) référencés par un Index, un par ligne avec un pipe (|) entre le timestamp et l'élément.
Faites les changements qui s'imposent dans la classe Index et vérifier que les tests marqués "Q4" passent.

5. On souhaite ajouter une autre méthode index(lambda) qui prend en paramètre une fonction/lambda qui est appelée sur chaque élément de la TimeSeries et indique si l'élément doit ou non faire partie de l'index.
Par exemple, avec une TimeSeries contenant les éléments "hello", "time" et "series" et une lambda s -> s.charAt(1) == 'e' qui renvoie vrai si le deuxième caractère est un 'e', l'Index renvoyé contient [0, 2].
Quel doit être le type du paramètre de la méthode index(lambda) ?
Écrire la méthode index(filter) et vérifier que les tests marqués "Q5" passent.
Note : On peut remarquer qu'il est possible de ré-écrire la méthode index sans paramètre pour utiliser celle avec un paramètre.

6. Dans la classe Index, écrire une méthode forEach(lambda) qui prend en paramètre une fonction/lambda qui est appelée avec chaque Data référencée par les indices de l'Index.
Par exemple, avec la TimeSeries contenant les Data (24 | "hello"), (34 | "time") et (70 | "series") et un Index [0, 2], la fonction sera appelée avec les Data (24 | "hello") et (70 | "series").
Quel doit être le type du paramètre de la méthode forEach(lambda) ?
Écrire la méthode forEach(lambda) dans la classe Index et vérifier que les tests marqués "Q6" passent.

7. On souhaite maintenant pouvoir parcourir tous les Data d'un Index en utilisant une boucle for comme ceci
```java
...
var index = timeSeries.index();
for(var data: index) {
System.out.println(data);
}
```

Quelle interface doit implanter la classe Index pour pouvoir être utilisée dans une telle boucle ?
Quelle méthode de l'interface doit-on implanter ? Et quel est le type de retour de cette méthode ? Faites les modifications qui s'imposent dans la classe Index et vérifiez que les tests marqués "Q7" passent.

8. On veut ajouter une méthode or sur un Index qui prend en paramètre un Index et renvoie un nouvel Index qui contient à la fois les indices de l'Index courant et les indices de l'Index passé en paramètre.
Il ne doit pas être possible de faire un or avec deux Index issus de TimeSeries différentes.
En termes d'implantation, on peut faire une implantation en O(n) mais elle est un peu compliquée à écrire. On se propose d'écrire une version en O(n.log(n)) en concaténant les Stream de chaque index puis en triant les indices et en retirant les doublons.
Expliquer pourquoi on ne peut pas juste concaténer les deux tableaux d'indices ?
Écrire le code de la méthode or(index) dans la classe Index et vérifier que les tests marqués "Q8" passent.
Pour concaténer des IntStream il existe une méthode IntStream.concat

9. Même question que précédemment, mais au lieu de vouloir faire un or, on souhaite faire un and entre deux Index.
En termes d'implantation, il existe un algorithme en O(n) qui est en O(1) en mémoire. À la place, nous allons utiliser un algorithme en O(n) mais qui utilise O(n) en mémoire. L'idée est de prendre un des tableaux d'indices et de stocker tous les indices dans un ensemble sans doublons puis de parcourir l'autre tableau d'indices et de vérifier que chaque indice est bien dans l'ensemble.
Écrire le code de la méthode and(index) dans la classe Index et vérifier que les tests marqués "Q9" passent.
Pour les plus balèzes, faites en sorte que les tests marqués "Q10" passent.