package fr.uge.table;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToIntFunction;

public final class IntTable {
	private record MapImpl(HashMap<String, Integer> map)  {
		public void set(String key, int value) {
			map.put(key, value);
		}

		public int get(String key, int orElse) {
			return map.getOrDefault(key, orElse);
		}

		public int size() {
			return map.size();
		}
	}
	private final MapImpl storage;
	public IntTable() {
		this.storage = new MapImpl(new HashMap<>());
	}

	private IntTable(MapImpl storage) {
		this.storage = storage;
	}

	public void set(String key, int value) {
		Objects.requireNonNull(key);
		storage.set(key, value);
	}

	public int size() {
		return storage.size();
	}

	public int get(String key, int orElse) {
		Objects.requireNonNull(key);
		return storage.get(key, orElse);
	}

	public IntTable apply(ToIntFunction<Integer> function) {
		Objects.requireNonNull(function);
		var newMap = new HashMap<String, Integer>();
		for (var pair : storage.map.entrySet()) {
			newMap.put(pair.getKey(), function.applyAsInt(pair.getValue()));
		}
		return new IntTable(new MapImpl(newMap));
	}

	static Map<String, Integer> recordComponentIndexes(RecordComponent[] recordComponents) {
		Objects.requireNonNull(recordComponents);
		var map = new HashMap<String, Integer>();
		for (int i = 0; i < recordComponents.length; i++) {
			map.put(recordComponents[i].getName(), i);
		}

		return map;
	}
}
