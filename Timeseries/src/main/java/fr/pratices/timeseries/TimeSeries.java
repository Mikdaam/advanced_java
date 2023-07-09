package fr.pratices.timeseries;

import java.util.Objects;

public class TimeSeries<E> {
	record Data<E> (long timestamp, E element) {
		Data {
			Objects.requireNonNull(element);
		}
	}


}
