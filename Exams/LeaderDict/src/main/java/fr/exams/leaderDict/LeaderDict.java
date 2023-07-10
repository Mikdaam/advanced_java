package fr.exams.leaderDict;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LeaderDict <L, E> {
	private final Function<? super E, L> leaderFunction;
	private final LinkedHashMap<L, LinkedList<E>> map;
	public LeaderDict(Function<? super E, L> function) {
		Objects.requireNonNull(function);
		this.leaderFunction = function;
		this.map = new LinkedHashMap<>();
	}

	public void add(E element) {
		Objects.requireNonNull(element);
		var leader = leaderFunction.apply(element);
		var leaderElements = map.computeIfAbsent(leader, l -> new LinkedList<>());
		leaderElements.add(element);
	}

	public int leaderCount() {
		return map.keySet().size();
	}

	public List<E> valuesFor(Object leader) {
		Objects.requireNonNull(leader);

		class SubList extends AbstractList<E> implements RandomAccess {
			private final List<E> list = map.getOrDefault((E) leader, new LinkedList<>());
			@Override
			public E get(int index) {
				return list.get(index);
			}

			@Override
			public int size() {
				return list.size();
			}
		}

		return new SubList();
	}

	public void forEach(BiConsumer<? super L, ? super E> biConsumer) {
		Objects.requireNonNull(biConsumer);
		for (var entry : map.entrySet()) {
			for (var element : entry.getValue()) {
				biConsumer.accept(entry.getKey(), element);
			}
		}
	}

	public void addAll(LeaderDict<? extends L, ? extends E> leaderDict) {
		Objects.requireNonNull(leaderDict);
		if (leaderFunction.equals(leaderDict.leaderFunction)) {
			for (var entry : leaderDict.map.entrySet()) {
				var currentElements = map.computeIfAbsent(entry.getKey(), l -> new LinkedList<>());
				currentElements.addAll(entry.getValue());
			}
		} else {
			leaderDict.forEach((l, e) -> add(e));
		}
	}

	public Stream<E> values() {
		var spliterator = map.values().stream().flatMap(Collection::stream).spliterator();
		return StreamSupport.stream(spliterator, true);
	}


	@Override
	public String toString() {
		var builder = new StringBuilder();
		/*for (var entry : map.entrySet()) {
			for (var element : entry.getValue()) {
				builder.append(entry.getKey()).append(": ").append(element).append("\n");
			}
		}*/
		forEach((leader, element) -> builder.append(leader).append(": ").append(element).append("\n"));

		return builder.toString();
	}
}
