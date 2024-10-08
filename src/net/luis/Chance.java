package net.luis;

import java.util.Random;

public class Chance {
	
	private final Random rng = new Random();
	private final double chance;
	
	private Chance(double chance) {
		this.chance = chance;
	}
	
	public static Chance of(double chance) {
		return new Chance(chance);
	}
	
	public void setSeed(long seed) {
		this.rng.setSeed(seed);
	}
	
	public boolean isTrue() {
		return this.chance >= 1.0;
	}
	
	public boolean isFalse() {
		return 0.0 >= this.chance;
	}
	
	public boolean result() {
		if (this.isTrue()) {
			return true;
		} else if (this.isFalse()) {
			return false;
		} else {
			return this.chance > this.rng.nextDouble();
		}
	}
	
}
