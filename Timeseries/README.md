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