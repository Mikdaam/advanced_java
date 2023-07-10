package fr.exams.leaderDict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class LeaderDict <L, E> {
	private final Function<? super E, L> leaderFunction;
	private final HashMap<L, List<E>> map;
	public LeaderDict(Function<? super E, L> function) {
		Objects.requireNonNull(function);
		this.leaderFunction = function;
		this.map = new HashMap<>();
	}

	public void add(E element) {
		Objects.requireNonNull(element);
		var leader = leaderFunction.apply(element);
		var leaderElements = map.computeIfAbsent(leader, l -> new ArrayList<>());
		leaderElements.add(element);
	}

	public int leaderCount() {
		return map.keySet().size();
	}
}
