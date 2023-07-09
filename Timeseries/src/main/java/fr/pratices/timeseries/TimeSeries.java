package fr.pratices.timeseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries<E> {
	record Data<E> (long timestamp, E element) {
		Data {
			Objects.requireNonNull(element);
		}
	}
	class Index {
		private final int[] indexes;

		private Index(int[] indexes) {
			Objects.requireNonNull(indexes);
			this.indexes = indexes;
		}

		public int size() {
			return indexes.length;
		}
	}

	private final ArrayList<Data<E>> data = new ArrayList<>();
	private long lastTimestamp;

	public void add(long timestamp, E element) {
		if (lastTimestamp != 0 && timestamp < lastTimestamp) {
			throw new IllegalStateException();
		}
		Objects.requireNonNull(element);
		lastTimestamp = timestamp;
		data.add(new Data<>(timestamp, element));
	}

	public int size() {
		return data.size();
	}

	public Data<E> get(int index) {
		Objects.checkIndex(index, data.size());
		return data.get(index);
	}

	public Index index() {
		var indexes = IntStream.range(0, data.size()).toArray();
		return new Index(indexes);
	}
}
