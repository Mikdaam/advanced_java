package fr.pratices.timeseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries<E> {
	record Data<E> (long timestamp, E element) {
		Data {
			Objects.requireNonNull(element);
		}

		@Override
		public String toString() {
			return timestamp + " | " + element;
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

		@Override
		public String toString() {
			return Arrays.stream(indexes)
					.mapToObj(data::get)
					.map(Data::toString)
					.collect(Collectors.joining("\n"));
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
		return index(e -> true);
	}

	public Index index(Predicate< ? super E> filter) {
		Objects.requireNonNull(filter);
		IntPredicate finalFilter = (int i) -> filter.test(data.get(i).element);
		var indexes = IntStream.range(0, data.size())
				.filter(finalFilter)
				.toArray();
		return new Index(indexes);
	}
}
