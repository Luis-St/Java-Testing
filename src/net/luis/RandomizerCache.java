package net.luis;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Luis-St
 *
 */

public class RandomizerCache<T> {
	
	private final int offset;
	private final List<T> values;
	
	public RandomizerCache(Random rng, List<T> values) {
		int offset;
		do {
			offset = rng.nextInt(Integer.MAX_VALUE);
		} while (offset % values.size() == 0);
		this.offset = offset;
		this.values = values;
	}
	
	public T getRandomized(T original) {
		int index = this.values.indexOf(original);
		if (index == -1) {
			throw new IllegalArgumentException("Value not found in cache");
		}
		
		int i = (index + this.offset) % this.values.size();
		System.out.println("index: " + index + ", offset: " + this.offset + ", i: " + i);
		return this.values.get(i);
	}
	
	public T getOriginal(T randomized) {
		int index = this.values.indexOf(randomized);
		if (index == -1) {
			throw new IllegalArgumentException("Value not found in cache");
		}
		/*int i = (index - this.offset) % this.values.size();
		int j = this.values.size() + i - 1;
		System.out.println("index: " + index + ", offset: " + this.offset + ", i: " + i + ", j: " + j);*/
		int i = (index - this.offset + this.values.size()) % this.values.size();
		if (0 > i) {
			i += this.values.size();
		}
		System.out.println("index: " + index + ", offset: " + this.offset + ", i: " + i);
		return this.values.get(i);
	}
}
