package net.luis;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.*;

/**
 *
 * @author Luis-St
 *
 */

public class BruteForce {
	
	private static final String ALL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String HASH = "75ee1ea2a9253c28b94341895efd933fc68711557d09987c6d4a7a04231a0f53"; // -> CZisAL
	
	public static void main(String[] args) {
		System.out.println("Starting Brute Force");
		int threads = Runtime.getRuntime().availableProcessors();
		try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
			for (char c : ALL.toCharArray()) {
				executor.submit(new Task(c));
			}
			executor.shutdown();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private record Task(char start) implements Runnable {
		
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			Thread.currentThread().setName(String.valueOf(this.start));
			System.out.println("Starting " + Thread.currentThread().getName());
			for (char b : ALL.toCharArray()) {
				System.out.println(Thread.currentThread().getName() + ":" + b);
				for (char c : ALL.toCharArray()) {
					for (char d : ALL.toCharArray()) {
						for (char e : ALL.toCharArray()) {
							for (char f : ALL.toCharArray()) {
								String password = String.valueOf(this.start) + b + c + d + e + f;
								String hash = sha256(password);
								if (hash.equals(HASH)) {
									System.out.println("Password: " + password);
									//System.exit(0);
								}
							}
						}
					}
				}
			}
			System.out.println("Finished " + Thread.currentThread().getName() + " in " + (System.currentTimeMillis() - start) + "ms");
		}
	}
	
	private static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1) {
					hexString.append('0');
					
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
