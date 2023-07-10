package fr.exams.leaderDict;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		return Collections.unmodifiableMap(map).getOrDefault(leader, new LinkedList<>());
	}

	@Override
	public String toString() {
		var builder = new StringBuilder();
		for (var entry : map.entrySet()) {
			for (var element : entry.getValue()) {
				builder.append(entry.getKey()).append(": ").append(element).append("\n");
			}
		}

		return builder.toString();
	}
}
