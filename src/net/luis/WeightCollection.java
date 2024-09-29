package net.luis;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightCollection<E> {
	
	private final NavigableMap<Integer, E> map;
	private final Random random;
	private int total = 0;
	
	public WeightCollection() {
		this(new Random());
	}
	
	public WeightCollection(Random random) {
		this.map = new TreeMap<>();
		this.random = random;
	}

	public WeightCollection<E> add(int weight, E result) {
		if (0 >= weight) {
			throw new IllegalArgumentException("The weight must be larger that 0 but it is " + weight);
		}
		this.total += weight;
		this.map.put(this.total, result);
		return this;
	}
	
	public E next() {
		int value = (int) (this.random.nextDouble() * this.total);
		return this.map.higherEntry(value).getValue();
	}
}
