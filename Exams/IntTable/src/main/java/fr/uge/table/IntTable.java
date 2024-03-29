package fr.uge.table;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
			var applyMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> function.applyAsInt(entry.getValue())));
			return new MapImpl(applyMap);
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
			int index = keys.get(key, -1);
			if (index == -1) {
				throw new IllegalStateException();
			}
			values[index] = value;
		}

		@Override
		public int get(String key, int orElse) {
			int index = keys.get(key, -1);
			return index == -1 ? -1 : values[index];
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
		return IntStream.range(0, recordComponents.length)
				.boxed()
				.collect(Collectors.toMap(i -> recordComponents[i].getName(), i -> i));
	}

	public static IntTable from(Class<?> recordClass) {
		Objects.requireNonNull(recordClass);
		if (!recordClass.isRecord()) {
			throw new IllegalArgumentException();
		}
		var keys = recordComponentIndexes(recordClass.getRecordComponents());
		return new IntTable(new RecordImpl(new MapImpl(keys)));
	}

	@Override
	public String toString() {
		return switch (storage) {
			case MapImpl map -> {
				yield  "";
			}
			case RecordImpl record -> {
				yield  "";
			}
		};
	}
}
