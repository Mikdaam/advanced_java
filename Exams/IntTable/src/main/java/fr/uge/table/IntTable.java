package fr.uge.table;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public final class IntTable {
	sealed interface Impl {
		void set(String key, int value);
		int get(String key, int orElse);
		int size();
		Impl apply(IntUnaryOperator function);
	}
	static final class MapImpl implements Impl {
		private final Map<String, Integer> map;

		private MapImpl(Map<String, Integer> map) {
			this.map = map;
		}
		@Override
		public void set(String key, int value) {
			map.put(key, value);
		}

		@Override
		public int get(String key, int orElse) {
			return map.getOrDefault(key, orElse);
		}

		@Override
		public int size() {
			return map.size();
		}

		@Override
		public Impl apply(IntUnaryOperator function) {
			var impl = new MapImpl(new HashMap<>());
			map.forEach((key, value) -> impl.set(key, function.applyAsInt(value)));
			return impl;
		}
	}
	static final class RecordImpl implements Impl {
		private final MapImpl keys;
		private final int[] values;

		public RecordImpl(MapImpl keys) {
			this.keys = keys;
			this.values = new int[keys.size()];
		}
		@Override
		public void set(String key, int value) {

		}

		@Override
		public int get(String key, int orElse) {
			return 0;
		}

		@Override
		public int size() {
			return keys.size();
		}

		@Override
		public Impl apply(IntUnaryOperator function) {
			return null;
		}
	}
	private final Impl storage;
	public IntTable() {
		this.storage = new MapImpl(new HashMap<>());
	}

	private IntTable(Impl storage) {
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

	public IntTable apply(IntUnaryOperator function) {
		Objects.requireNonNull(function);
		return new IntTable(storage.apply(function));
	}

	static Map<String, Integer> recordComponentIndexes(RecordComponent[] recordComponents) {
		Objects.requireNonNull(recordComponents);
		var map = new HashMap<String, Integer>();
		for (int i = 0; i < recordComponents.length; i++) {
			map.put(recordComponents[i].getName(), i);
		}
		return map;
	}

	public static IntTable from(Class<?> recordClass) {
		if (!recordClass.isRecord()) {
			throw new IllegalArgumentException();
		}
		var keys = recordComponentIndexes(recordClass.getRecordComponents());
		return new IntTable(new RecordImpl(new MapImpl(keys)));
	}
}
