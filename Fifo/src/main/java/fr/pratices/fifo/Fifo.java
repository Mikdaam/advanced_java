package fr.pratices.fifo;

public class Fifo<E> {
	private final int maxElements;
	public Fifo(int maxElements) {
		if (maxElements <= 0) {
			throw new IllegalArgumentException();
		}
		this.maxElements = maxElements;
	}

}
