package fr.exams.leaderDict;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderDictTest {

  // Q1

  @Test @Tag("Q1")
  public void leaderDictOfEmployee() {
    record Employee(String name, String manager) {}
    LeaderDict<String, Employee> leaderDict = new LeaderDict<>(Employee::manager);
    assertNotNull(leaderDict);
  }
  @Test @Tag("Q1")
  public void leaderDictOfIntegers() {
    LeaderDict<Integer, Integer> leaderDict = new LeaderDict<>(x -> x % 10);
    assertNotNull(leaderDict);
  }
  @Test @Tag("Q1")
  public void addEmployee() {
    record Employee(String name, String manager) {}
    var leaderDict = new LeaderDict<>(Employee::manager);
    var bob = new Employee("Bob", "Dan");
    var ana = new Employee("Ana", "Dan");
    var guy = new Employee("Guy", "Bob");
    leaderDict.add(bob);
    leaderDict.add(ana);
    leaderDict.add(guy);
    assertEquals(2, leaderDict.leaderCount());
  }
  @Test @Tag("Q1")
  public void addPerson() {
    record Person(String name, String petName) {}
    var leaderDict = new LeaderDict<>(Person::petName);
    leaderDict.add(new Person("Ana", "Lady"));
    leaderDict.add(new Person("Bob", "Ugly"));
    assertEquals(2, leaderDict.leaderCount());
  }
  @Test @Tag("Q1")
  public void addPerson2() {
    record Person(String name, String petName) {}
    var leaderDict = new LeaderDict<>(Person::petName);
    leaderDict.add(new Person("Ana", "Lady"));
    leaderDict.add(new Person("Bob", "Ugly"));
    leaderDict.add(new Person("Guy", "Lady"));
    assertEquals(2, leaderDict.leaderCount());
  }
  @Test @Tag("Q1")
  public void leaderCount() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    assertEquals(0, leaderDict.leaderCount());
  }
  @Test @Tag("Q1")
  public void leaderDictSignature() {
    var leaderDict = new LeaderDict<CharSequence, Integer>((Object x) -> x.toString());
    leaderDict.add(3);
    assertEquals(1, leaderDict.leaderCount());
  }
  @Test @Tag("Q1")
  public void leaderDictPrecondition() {
    assertThrows(NullPointerException.class, () -> new LeaderDict<>(null));
  }
  @Test @Tag("Q1")
  public void addPrecondition() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    assertThrows(NullPointerException.class, () -> leaderDict.add(null));
  }


  // Q2

  @Test @Tag("Q2")
  public void valuesForStringLength() {
    LeaderDict<Integer, String> leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foo");
    List<String> values = leaderDict.valuesFor(3);
    assertEquals(List.of("foo"), values);
  }
  @Test @Tag("Q2")
  public void valuesForPerson() {
    record Person(String name, String petName) {}
    var leaderDict = new LeaderDict<>(Person::petName);
    var ana = new Person("Ana", "Lady");
    var bob = new Person("Bob", "Ugly");
    leaderDict.add(ana);
    leaderDict.add(bob);
    assertAll(
        () -> assertEquals(List.of(ana), leaderDict.valuesFor("Lady")),
        () -> assertEquals(List.of(bob), leaderDict.valuesFor("Ugly")),
        () -> assertEquals(List.of(), leaderDict.valuesFor("Snoopy"))
    );
  }
  @Test @Tag("Q2")
  public void valuesForEmployee() {
    record Employee(String name, String manager) {}
    var leaderDict = new LeaderDict<>(Employee::manager);
    var bob = new Employee("Bob", "Dan");
    var ana = new Employee("Ana", "Dan");
    var guy = new Employee("Guy", "Bob");
    leaderDict.add(bob);
    leaderDict.add(ana);
    leaderDict.add(guy);
    assertAll(
        () -> assertEquals(List.of(bob, ana), leaderDict.valuesFor("Dan")),
        () -> assertEquals(List.of(guy), leaderDict.valuesFor("Bob")),
        () -> assertEquals(List.of(), leaderDict.valuesFor("James"))
    );
  }
  @Test @Tag("Q2")
  public void valuesForIntegers() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    leaderDict.add(102);
    leaderDict.add(42);
    assertAll(
        () -> assertEquals(List.of(102, 42), leaderDict.valuesFor(2)),
        () -> assertEquals(List.of(), leaderDict.valuesFor(7))
    );
  }
  @Test @Tag("Q2")
  public void valuesForStringNonModifiableAfterCreation() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foo");
    var values = leaderDict.valuesFor(3);
    leaderDict.add("bar");
    assertEquals(List.of("foo"), values);
  }
  @Test @Tag("Q2")
  public void valuesForSignature() {
    record Employee(String name, String manager) {}
    LeaderDict<String, Employee> leaderDict = new LeaderDict<>(Employee::manager);
    leaderDict.add(new Employee("Ana", "Dan"));
    leaderDict.add(new Employee("Bob", "Dan"));
    assertEquals(List.of(), leaderDict.valuesFor(404));
  }
  @Test @Tag("Q2")
  public void valuesForPrecondition() {
    var leaderDict = new LeaderDict<>(String::length);
    assertThrows(NullPointerException.class, () -> leaderDict.valuesFor(null));
  }


  // Q3

  @Test @Tag("Q3")
  public void printIntegers() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    leaderDict.add(102);
    leaderDict.add(777);
    leaderDict.add(42);
    assertEquals("""
        2: 102
        2: 42
        7: 777
        """, "" + leaderDict);
  }
  @Test @Tag("Q3")
  public void printIntegersOrder() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    leaderDict.add(747);
    leaderDict.add(102);
    leaderDict.add(2);
    leaderDict.add(777);
    leaderDict.add(42);
    assertEquals("""
        7: 747
        7: 777
        2: 102
        2: 2
        2: 42
        """, "" + leaderDict);
  }
  @Test @Tag("Q3")
  public void printEmployee() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foobar");
    leaderDict.add("foo");
    leaderDict.add("bar");
    assertEquals("""
        6: foobar
        3: foo
        3: bar
        """, "" + leaderDict);
  }
  @Test @Tag("Q3")
  public void printEmpty() {
    var leaderDict = new LeaderDict<>(String::length);
    assertEquals("", "" + leaderDict);
  }


  // Q4

  /*@Test @Tag("Q4")
  public void forEachStringLength() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foo");
    leaderDict.add("whizz");
    leaderDict.add("clara");
    leaderDict.add("bar");
    var all = new ArrayList<>();
    leaderDict.forEach((length, string) -> {
      all.add(length);
      all.add(string);
    });
    assertEquals(List.of(3, "foo", 3, "bar", 5, "whizz", 5, "clara"), all);
  }
  @Test @Tag("Q4")
  public void forEachEmployee() {
    record Employee(String name, String manager) {}
    var leaderDict = new LeaderDict<>(Employee::manager);
    var ana = new Employee("Ana", "Dan");
    var guy = new Employee("Guy", "Bob");
    var bob = new Employee("Bob", "Dan");
    leaderDict.add(ana);
    leaderDict.add(guy);
    leaderDict.add(bob);
    var managers = new ArrayList<String>();
    var employees = new ArrayList<Employee>();
    leaderDict.forEach((manager, employee) -> {
      managers.add(manager);
      employees.add(employee);
    });
    assertAll(
        () -> assertEquals(List.of("Dan", "Dan", "Bob"), managers),
        () -> assertEquals(List.of(ana, bob, guy), employees)
    );
  }
  @Test @Tag("Q4")
  public void forEachSignature() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    leaderDict.add(42);
    leaderDict.forEach((Object leader, Object element) -> {
      assertEquals(2, leader);
      assertEquals(42, element);
    });
  }
  @Test @Tag("Q4")
  public void forEachEmpty() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    leaderDict.forEach((_1, _2) -> fail());
  }
  @Test @Tag("Q4")
  public void forEachPrecondition() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    assertThrows(NullPointerException.class, () -> leaderDict.forEach(null));
  }


  // Q6

  @Test @Tag("Q6")
  public void valuesForStringNonMutable() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foo");
    leaderDict.add("bar");
    var values = leaderDict.valuesFor(3);
    assertAll(
        () -> assertThrows(UnsupportedOperationException.class, () -> values.set(0, "baz")),
        () -> assertThrows(UnsupportedOperationException.class, () -> values.add("baz")),
        () -> assertThrows(UnsupportedOperationException.class, () -> values.remove("foo")),
        () -> assertThrows(UnsupportedOperationException.class, () -> values.addAll(List.of("foo"))),
        () -> assertThrows(UnsupportedOperationException.class, () -> {
          var iterator = values.listIterator();
          iterator.next();
          iterator.set("baz");
        }),
        () -> assertThrows(UnsupportedOperationException.class, () -> {
          var iterator = values.listIterator();
          iterator.next();
          iterator.add("baz");
        }),
        () -> assertThrows(UnsupportedOperationException.class, () -> {
          var iterator = values.listIterator();
          iterator.next();
          iterator.remove();
        })
    );
  }
  @Test @Tag("Q6")
  public void valuesWithALotOfElements() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> 42);
    assertTimeoutPreemptively(Duration.ofMillis(1_000), () -> {
      for (var i = 0; i < 1_000_000; i++) {
        leaderDict.add(i);
        var list = leaderDict.valuesFor(42);
        assertEquals(0, list.get(0));
      }
    });
  }
  @Test @Tag("Q6")
  public void valuesForStringHasConstantRandomAccess() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foo");
    var values = leaderDict.valuesFor(3);
    assertTrue(values instanceof RandomAccess);
  }
  @Test @Tag("Q6")
  public void valuesForStringHasConstantRandomAccessEvenIfEmpty() {
    var leaderDict = new LeaderDict<>(String::length);
    leaderDict.add("foobar");
    var values = leaderDict.valuesFor(42);
    assertTrue(values instanceof RandomAccess);
  }


  // Q7

  private static LeaderDict<Integer, String> createStringLengthLeaderDict() {
    return new LeaderDict<>(String::length);
  }

  @Test @Tag("Q7")
  public void addAll() {
    var leaderDict = createStringLengthLeaderDict();
    leaderDict.add("foo");
    leaderDict.add("hello");
    var leaderDict2 = createStringLengthLeaderDict();
    leaderDict2.add("bar");
    leaderDict2.add("dang");
    leaderDict2.add("whizz");
    leaderDict.addAll(leaderDict2);
    assertAll(
        () -> assertEquals(3, leaderDict.leaderCount()),
        () -> assertEquals(List.of("foo", "bar"), leaderDict.valuesFor(3)),
        () -> assertEquals(List.of("dang"), leaderDict.valuesFor(4)),
        () -> assertEquals(List.of("hello", "whizz"), leaderDict.valuesFor(5))
    );
  }
  @Test @Tag("Q7")
  public void addAllData() {
    record Data(String field1, String field2) {}
    var leaderDict = new LeaderDict<>(Data::field1);
    leaderDict.add(new Data("foo", "bar"));
    leaderDict.add(new Data("foo", "baz"));
    leaderDict.add(new Data("hello", "whizz"));
    var leaderDict2 = new LeaderDict<>(Data::field2);
    leaderDict2.add(new Data("foo", "bar"));
    leaderDict2.add(new Data("foobar", "fuzz"));
    leaderDict.addAll(leaderDict2);
    assertAll(
        () -> assertEquals(3, leaderDict.leaderCount()),
        () -> assertEquals(List.of("bar", "baz", "bar"), leaderDict.valuesFor("foo").stream().map(Data::field2).toList()),
        () -> assertEquals(List.of("whizz"), leaderDict.valuesFor("hello").stream().map(Data::field2).toList()),
        () -> assertEquals(List.of("fuzz"), leaderDict.valuesFor("foobar").stream().map(Data::field2).toList())
    );
  }
  @Test @Tag("Q7")
  public void addAllEmpty() {
    record Employee(String name, String manager) {
    }
    var leaderDict = new LeaderDict<>(Employee::manager);
    leaderDict.add(new Employee("Ana", "Dan"));
    var leaderDict2 = new LeaderDict<>(Employee::manager);
    leaderDict.addAll(leaderDict2);
    assertAll(
        () -> assertEquals(1, leaderDict.leaderCount()),
        () -> assertEquals(List.of(new Employee("Ana", "Dan")), leaderDict.valuesFor("Dan"))
    );
  }
  @Test @Tag("Q7")
  public void addAllNoSharing() {
    var leaderDict = createStringLengthLeaderDict();
    leaderDict.add("hello");
    var leaderDict2 = createStringLengthLeaderDict();
    leaderDict2.add("foo");
    leaderDict2.add("bar");
    leaderDict.addAll(leaderDict2);
    leaderDict.add("baz");
    assertAll(
        () -> assertEquals(2, leaderDict.leaderCount()),
        () -> assertEquals(List.of("hello"), leaderDict.valuesFor(5)),
        () -> assertEquals(List.of("foo", "bar", "baz"), leaderDict.valuesFor(3)),
        () -> assertEquals(1, leaderDict2.leaderCount()),
        () -> assertEquals(List.of("foo", "bar"), leaderDict2.valuesFor(3))
    );
  }
  @Test @Tag("Q7")
  public void addAllNoSharing2() {
    var leaderDict = createStringLengthLeaderDict();
    leaderDict.add("hello");
    var leaderDict2 = createStringLengthLeaderDict();
    leaderDict2.add("foo");
    leaderDict2.add("bar");
    leaderDict.addAll(leaderDict2);
    leaderDict2.add("baz");
    assertAll(
        () -> assertEquals(2, leaderDict.leaderCount()),
        () -> assertEquals(List.of("hello"), leaderDict.valuesFor(5)),
        () -> assertEquals(List.of("foo", "bar"), leaderDict.valuesFor(3)),
        () -> assertEquals(1, leaderDict2.leaderCount()),
        () -> assertEquals(List.of("foo", "bar", "baz"), leaderDict2.valuesFor(3))
    );
  }
  @Test @Tag("Q7")
  public void addAllSameFunction() {
    var operator = new UnaryOperator<String>() {
      boolean shouldNotBeCalled;

      @Override
      public String apply(String s) {
        if (shouldNotBeCalled) {
          fail();
        }
        return s;
      }
    };
    var leaderDict = new LeaderDict<>(operator);
    leaderDict.add("foo");
    leaderDict.add("bar");
    var leaderDict2 = new LeaderDict<>(operator);
    leaderDict2.add("foo");
    leaderDict2.add("baz");
    operator.shouldNotBeCalled = true;
    leaderDict.addAll(leaderDict2);
    assertAll(
        () -> assertEquals(3, leaderDict.leaderCount()),
        () -> assertEquals(List.of("foo", "foo"), leaderDict.valuesFor("foo")),
        () -> assertEquals(List.of("bar"), leaderDict.valuesFor("bar")),
        () -> assertEquals(List.of("baz"), leaderDict.valuesFor("baz"))
    );
  }
  @Test @Tag("Q7")
  public void addAllHierarchy() {
    interface Party { String manager(); }
    record Employee(String name, String manager) implements Party {}
    record Company(String name, String manager) implements Party {}
    var hugs = new Company("Hugs", "Dan");
    var ana = new Employee("Ana", "Ross");
    var bob = new Employee("Bob", "Dan");
    var leaderDict = new LeaderDict<CharSequence, Party>(Party::manager);
    leaderDict.add(hugs);
    var leaderDict2 = new LeaderDict<>(Employee::manager);
    leaderDict2.add(ana);
    leaderDict.add(bob);
    leaderDict.addAll(leaderDict2);
    assertAll(
        () -> assertEquals(2, leaderDict.leaderCount()),
        () -> assertEquals(List.of(hugs, bob), leaderDict.valuesFor("Dan")),
        () -> assertEquals(List.of(ana), leaderDict.valuesFor("Ross"))
    );
  }
  @Test @Tag("Q7")
  public void addAllPrecondition() {
    var leaderDict = new LeaderDict<>(String::length);
    assertThrows(NullPointerException.class, () -> leaderDict.addAll(null));
  }


  // Q8

  @Test @Tag("Q8")
  public void valuesEmployee() {
    record Employee(String name, String manager) {}
    var leaderDict = new LeaderDict<>(Employee::manager);
    var ana = new Employee("Ana", "Dan");
    var guy = new Employee("Guy", "Bob");
    var bob = new Employee("Bob", "Dan");
    leaderDict.add(ana);
    leaderDict.add(guy);
    leaderDict.add(bob);
    assertEquals(List.of(ana, bob, guy), leaderDict.values().toList());
  }
  @Test @Tag("Q8")
  public void valuesInteger() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    IntStream.range(0, 100).forEach(leaderDict::add);
    var expected = IntStream.range(0, 10).boxed()
        .flatMap(i -> Stream.of(i, i + 10, i + 20, i + 30, i + 40, i + 50, i + 60, i + 70, i + 80, i + 90))
        .toList();
    assertEquals(expected, leaderDict.values().toList());
  }
  @Test @Tag("Q8")
  public void valuesEmpty() {
    var leaderDict = new LeaderDict<>(String::length);
    assertEquals(0L, leaderDict.values().count());
  }
  @Test @Tag("Q8")
  public void characteristics() {
    var leaderDict = new LeaderDict<>(String::length);
    var list = new ArrayList<>();
    assertEquals(1, Integer.bitCount(leaderDict.values().spliterator().characteristics() & list.spliterator().characteristics()));
  }


  // Q9

  @Test @Tag("Q9")
  public void valuesParallelALotOfValues() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x);
    IntStream.range(0, 1_000_000).forEach(leaderDict::add);
    assertEquals(IntStream.range(0, 1_000_000).boxed().toList(), leaderDict.values().parallel().toList());
  }
  @Test @Tag("Q9")
  public void valuesParallelALotOfValuesSorted() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 1_000);
    IntStream.range(0, 1_000_000).forEach(leaderDict::add);
    assertEquals(IntStream.range(0, 1_000_000).boxed().toList(), leaderDict.values().parallel().sorted().toList());
  }
  @Test @Tag("Q9")
  public void valuesSplittable() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x);
    IntStream.range(0, 1_000_000).forEach(leaderDict::add);
    assertNotNull(leaderDict.values().spliterator().trySplit());
  }
  @Test @Tag("Q9")
  public void valuesSplittable2() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> 42);
    IntStream.range(0, 1_000_000).forEach(leaderDict::add);
    assertNotNull(leaderDict.values().spliterator().trySplit());
  }
  @Test @Tag("Q9")
  public void valuesParallelEmployee() {
    record Employee(String name, String manager) {}
    var leaderDict = new LeaderDict<>(Employee::manager);
    var ana = new Employee("Ana", "Dan");
    var guy = new Employee("Guy", "Bob");
    var bob = new Employee("Bob", "Dan");
    leaderDict.add(ana);
    leaderDict.add(guy);
    leaderDict.add(bob);
    assertEquals(List.of(ana, bob, guy), leaderDict.values().parallel().toList());
  }
  @Test @Tag("Q9")
  public void valuesParallelInteger() {
    var leaderDict = new LeaderDict<Integer, Integer>(x -> x % 10);
    IntStream.range(0, 100).forEach(leaderDict::add);
    var expected = IntStream.range(0, 10).boxed()
        .flatMap(i -> Stream.of(i, i + 10, i + 20, i + 30, i + 40, i + 50, i + 60, i + 70, i + 80, i + 90))
        .toList();
    assertEquals(expected, leaderDict.values().parallel().toList());
  }
  @Test @Tag("Q9")
  public void valuesParallelEmpty() {
    var leaderDict = new LeaderDict<>(String::length);
    assertEquals(0L, leaderDict.values().parallel().count());
  }*/
}