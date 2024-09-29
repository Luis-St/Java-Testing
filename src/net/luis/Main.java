package net.luis;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("unused")
public class Main {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	public static void main(String[] args) throws Exception {
		//
		String bin = Integer.toBinaryString(Float.floatToFloat16(9876.54321F));
		System.out.println(bin.length());
		System.out.println(bin);
	}
	
	public static void testRegexSplit() {
		String test = "This:is a:test";
		System.out.println(Arrays.toString(test.split(":|\\s")));
	}
	
	public static void isAssignableFrom() {
		System.out.println(List.class.isAssignableFrom(List.class));
		System.out.println(List.class.isAssignableFrom(ArrayList.class));
		System.out.println(ArrayList.class.isAssignableFrom(List.class));
	}
	
	public static void arrayCreation() {
		int[] dimensions = IntStream.range(0, 3).map(i -> 0).toArray();
		System.out.println(Arrays.toString(dimensions));
		Object array = Array.newInstance(int.class, dimensions);
		
		int[][] ints = (int[][]) array;
		System.out.println(ints.length);
	}
	
	public static void randomizer() {
		List<String> values = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
		
		int randomizedIsOriginal = 0;
		int errors = 0;
		
		for (int i = 0; i < 100000; i++) {
			RandomizerCache<String> cache = new RandomizerCache<>(new Random(), values);
			for (String value : values) {
				String randomized = cache.getRandomized(value);
				if (value.equals(randomized)) {
					randomizedIsOriginal++;
				}
				String original = cache.getOriginal(randomized);
				if (!value.equals(original)) {
					errors++;
				}
				System.out.println(value + "-> R:" + randomized + " O:" + original);
			}
		}
		System.out.println("Randomized is original: " + randomizedIsOriginal);
		System.out.println("Errors: " + errors);
	}
	
	public static void calculateEulersNumber(long minutes, int precision) {
		BigDecimal one = BigDecimal.ONE;
		MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
		long startTime = System.currentTimeMillis();
		long endTime = startTime + (minutes * 60 * 1000);
		BigDecimal i = BigDecimal.ONE;
		while (System.currentTimeMillis() < endTime) {
			BigDecimal result = pow(one.add(one.divide(i, mc)), i, mc);
			System.out.println(result);
			i = i.add(BigDecimal.ONE);
		}
	}
	
	private static BigDecimal pow(BigDecimal base, BigDecimal exponent, MathContext mc) {
		BigDecimal result = BigDecimal.ONE;
		for (BigDecimal i = BigDecimal.ZERO; i.compareTo(exponent) < 0; i = i.add(BigDecimal.ONE)) {
			result = result.multiply(base, mc);
		}
		return result;
	}
	
	public static void stackTraceTest() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			System.out.println(i + ": " + elements[i].getMethodName());
		}
	}
	
	public static void packageTest() {
		System.out.println(Main.class.getPackageName());
		System.out.println(Arrays.stream("net.luis.utils.".split("\\.")).limit(1).collect(Collectors.joining(".")));
	}
	
	public static void byte2HexTest() {
		byte min = Byte.MIN_VALUE;
		byte def = 0;
		byte max = Byte.MAX_VALUE;
		System.out.println(min + ":" + Arrays.toString(toHex(min)) + ":" + toByte(toHex(min)));
		System.out.println(def + ":" + Arrays.toString(toHex(def)) + ":" + toByte(toHex(def)));
		System.out.println(max + ":" + Arrays.toString(toHex(max)) + ":" + toByte(toHex(max)));
	}
	
	private static byte[] toHex(byte b) {
		byte[] hex = new byte[2];
		hex[0] = (byte) ((b & 0xF0) >> 4);
		hex[1] = (byte) (b & 0x0F);
		return hex;
	}
	
	private static byte toByte(byte[] hex) {
		byte b = 0;
		b |= (byte) (hex[0] << 4);
		b |= hex[1];
		return b;
	}
	
	public static void keyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024 * 2 * 2 * 2);
		KeyPair keyPair = generator.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		
		System.out.println("Private: " + privateKey);
		System.out.println("Public: " + publicKey);
	}
	
	private static int absModulo(int value, int modulo) {
		return (value % modulo + modulo) % modulo;
	}
	
	public static void parseNumber() {
		String line = "1bskshtjseven5qlbjhqgzhbzxvlxsvtcmmzseven";
		int index = line.indexOf("seven");
		System.out.println(index);
		String sub = line.substring(index + "seven".length());
		System.out.println(sub);
	}
	
	public static void fillList() {
		List<Integer> list = new ArrayList<>();
		long start = System.nanoTime();
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			if (i % 2 != 0) {
				continue;
			}
			list.add(i);
			if (list.size() > 500) {
				list.clear();
			}
		}
		long end = System.nanoTime();
		System.out.println("Time: " + (end - start) + "ns");
	}
	
	public static void testSplit() {
		List<Integer> parts = split(21, 5);
		System.out.println(parts);
		System.out.println(parts.stream().mapToInt(Integer::intValue).sum());
	}
	
	public static List<Integer> split(int value, int parts) {
		int partValue = value / parts;
		int remaining = value % parts;
		return IntStream.range(0, parts).map(i -> i < remaining ? partValue + 1 : partValue).boxed().collect(Collectors.toList());
	}
	
	public static void testCirclePoints() {
		List<double[]> points = circlePoints(10, 0, 0, 8);
		for (double[] point : points) {
			System.out.println(Arrays.toString(point));
		}
	}
	
	public static List<double[]> circlePoints(double r, double centerX, double centerZ, int numOfPoints) {
		List<double[]> points = new ArrayList<>();
		double slice = 2 * Math.PI / numOfPoints;
		
		for (int i = 0; i < numOfPoints; i++) {
			double angle = slice * i;
			double newX = centerX + r * Math.cos(angle);
			double newZ = centerZ + r * Math.sin(angle);
			points.add(new double[] { newX, newZ });
		}
		
		return points;
	}
	
	public static double[] offsetPoint(double centerX, double centerZ, double radius) {
		Random random = new Random();
		double angle = Math.toRadians(random.nextFloat() * 360);
		
		double newX = centerX + radius * Math.cos(angle);
		double newZ = centerZ + radius * Math.sin(angle);
		return new double[] { newX, newZ };
	}
	
	public static void fileCopyPrank() throws Exception {
		Path temp = Files.createTempDirectory("temp");
		temp.toFile().deleteOnExit();
		File file = new File(temp.toFile(), "test.sha1");
		file.deleteOnExit();
		FileWriter writer = new FileWriter(file);
		writer.write(generateSHA1("Alles gute zum Geburtstag!"));
		writer.close();
		Path target = Paths.get("G:/");
		long start = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(24);
		for (int i = 0; i < 24; i++) {
			service.submit(() -> {
				System.out.println("Thread: " + Thread.currentThread().getName());
				while (true) {
					try {
						Files.copy(file.toPath(), target.resolve(UUID.randomUUID().toString() + ".sha1"), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						break;
					}
				}
			});
		}
		service.shutdown();
		try {
			service.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException ignored) {
		
		}
		long end = System.currentTimeMillis();
		System.out.println("Time: " + DATE_FORMAT.format(new Date((end - start))) + "ms");
		System.out.println("Files: " + Objects.requireNonNull(target.toFile().listFiles()).length);
	}
	
	public static String generateSHA1(String text) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] encodedHash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();
		
		for (byte b : encodedHash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append("0");
			}
			hexString.append(hex);
		}
		
		return hexString.toString();
	}
	
	public static void computeIfAbsent() {
		HashMap<String, List<Integer>> map = new HashMap<>();
		map.computeIfAbsent("Test", k -> new ArrayList<>()).add(1);
		map.computeIfAbsent("Test", k -> new ArrayList<>()).add(2);
		map.computeIfAbsent("Test", k -> new ArrayList<>()).add(3);
		System.out.println(map);
	}
	
	public static void runtimeCryption() throws Exception {
		String input = "Hello World";
		System.out.println("Input: " + input);
		String encrypted = Cryption.encrypt(input);
		System.out.println("Encrypted: " + encrypted);
		String decrypted = Cryption.decrypt(encrypted);
		System.out.println("Decrypted: " + decrypted);
	}
	
	public static class Task implements Runnable {
		
		public boolean isDone() {
			return false;
		}
		
		public boolean isCancelled() {
			return false;
		}
		
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}
		
		public boolean success() {
			return false;
		}
		
		public boolean failed() {
			return false;
		}
		
		@Override
		public void run() {
		
		}
	}
	
	public static boolean isArray(List<String> values) {
		boolean isArray = true;
		boolean hasDuplicates = new HashSet<>(values).size() != values.size();
		String name = values.get(0);
		for (int i = 1; i < values.size(); i++) {
			if (!name.equals(values.get(i))) {
				isArray = false;
				break;
			}
		}
		if (!isArray && hasDuplicates) {
			throw new RuntimeException("Not an array, but has duplicates");
		}
		return isArray;
	}
	
	public static void createComment() {
		List<String> comments = new ArrayList<>();
		comments.add("This is a comment");
		comments.add("This is another comment");
		System.out.println("# " + String.join(System.lineSeparator() + "# ", comments));
	}
	
	public static String centerString(String string, int size) {
		if (string.length() == size) {
			return string;
		}
		int remaining = size - string.length();
		if (remaining % 2 == 0) {
			return " ".repeat(remaining / 2) + string + " ".repeat(remaining / 2);
		} else {
			return " ".repeat(remaining / 2) + string + " ".repeat((remaining / 2) + 1);
		}
	}
	
	public static void calendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.DECEMBER, 23, 16, 56);
		System.out.println(calendar.getTime());
		calendar.add(Calendar.DAY_OF_WEEK, 7);
		Date date = calendar.getTime();
		System.out.println(date);
		calendar.setTime(date);
		System.out.println(calendar.getTime());
	}
	
	public static void superClasses() {
		Class<?> superClass = ArrayList.class;
		while ((superClass = superClass.getSuperclass()) != null) {
			System.out.println(superClass);
		}
		superClass = ArrayList.class;
		System.out.println();
		do {
			System.out.println(superClass);
		} while ((superClass = superClass.getSuperclass()) != null);
	}
	
	public static void testPrimitiveReflection(boolean flag) {
		System.out.println(flag);
	}
	
	public static void splitString() {
		String string = "#getGame#getMap#getFields";
		System.out.println(Arrays.toString(string.split("#")));
	}
	
	public static void assignable() {
		System.out.println(List.class.isAssignableFrom(ArrayList.class));
		System.out.println(ArrayList.class.isAssignableFrom(List.class));
	}
	
	public static void randomTest() {
		Random rng = new Random();
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < 1000000; i++) {
			values.add(randomInclusiveInt(rng, 0, 10));
		}
		System.out.println("Min: " + Collections.min(values));
		System.out.println("Max: " + Collections.max(values));
	}
	
	public static void cast() {
		int i = 10;
		System.out.println((Object) i instanceof Integer);
	}
	
	public static void substring() {
		System.out.println("@minecraft".substring(1));
		String name = "isLastName".substring("is".length());
		System.out.println(name.substring(0, 1).toLowerCase() + name.substring(1));
	}
	
	public static void cycle() {
		List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		int current = integers.get(0);
		for (int i = 0; i < 20; i++) {
			System.out.println(current);
			current = integers.get((integers.indexOf(current) + 1) % integers.size());
		}
	}
	
	public static void whileIterator() {
		List<String> strings = new ArrayList<>(Arrays.asList("VDE", "DABW", "ABE", "ABC", "D", "C", "E", "G", "X", "P", "H", "L", "5"));
		
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			if (string.length() > 1) {
				iterator.remove();
			}
		}
		System.out.println(strings);
	}
	
	public static void sorting() {
		List<Integer> integers = new ArrayList<>(Arrays.asList(0, 9, 3, 6, 7, 1, 8, 4, 5, 2, 100, 200));
		integers.sort((firstValue, secondValue) -> {
			if (secondValue > firstValue) {
				return -1;
			} else if (firstValue.equals(secondValue)) {
				return 0;
			} else {
				if (firstValue == 200) {
					return -2;
				}
				return 1;
			}
		});
		System.out.println(integers);
		
		List<String> strings = new ArrayList<>(Arrays.asList("VDE", "DABW", "ABE", "ABC", "D", "C", "E", "G", "X", "P", "H", "L", "5"));
		Collections.sort(strings);
		System.out.println(strings);
	}
	
	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	public static void timeAndDate() {
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		System.out.println(date);
		System.out.println(time);
	}
	
	public static void weightTest() {
		WeightCollection<String> collection = new WeightCollection<String>().add(80, "A").add(10, "B").add(5, "C").add(2, "D").add(2, "E").add(1, "F");
		double a = 0;
		double b = 0;
		double c = 0;
		double d = 0;
		double e = 0;
		double f = 0;
		for (int i = 0; i < 10000; i++) {
			for (int j = 0; j < 100; j++) {
				switch (collection.next()) {
					case "A" -> a++;
					case "B" -> b++;
					case "C" -> c++;
					case "D" -> d++;
					case "E" -> e++;
					case "F" -> f++;
					default -> throw new IllegalArgumentException("Unexpected value: " + collection.next());
				}
			}
		}
		System.out.println("A: " + (a / 100) / 10000);
		System.out.println("B: " + (b / 100) / 10000);
		System.out.println("C: " + (c / 100) / 10000);
		System.out.println("D: " + (d / 100) / 10000);
		System.out.println("E: " + (e / 100) / 10000);
		System.out.println("F: " + (f / 100) / 10000);
	}
	
	public static void chanceTest() {
		Chance chance = Chance.of(0.01);
		double d = 0.0;
		for (int i = 0; i < 10000000; i++) {
			for (int j = 0; j < 100; j++) {
				if (chance.result()) {
					d++;
				}
			}
		}
		System.out.println((d / 100.0) / 10000000.0);
	}
	
	public static void cutDouble() {
		double d = 0.005;
		String s = Double.toString(d).split("\\.")[1];
		if (s.length() > 2) {
			throw new IllegalArgumentException("Chance to small " + s);
		}
	}
	
	public static void minMax() {
		for (int i = -5; i <= 5; i += 5) {
			System.out.println("value " + i);
			System.out.println("min/min " + Math.min(-4, Math.min(i, 4)));
			System.out.println("min/max " + Math.min(-4, Math.max(i, 4)));
			System.out.println("max/min " + Math.max(4, Math.min(i, -4)));
			System.out.println("max/max " + Math.max(4, Math.max(i, -4)));
		}
	}
	
	public static abstract class A {
		
		protected final int i;
		
		protected A(int i) {
			System.out.println("Constructor in A");
			this.i = i;
			System.out.println("Before init call");
			this.init();
			System.out.println("After init call");
		}
		
		protected abstract void init();
		
	}
	
	public static class B extends A {
		
		public final int j;
		
		public B(int i) {
			super(i);
			System.out.println("After super in B");
			this.j = 10;
		}
		
		@Override
		protected void init() {
			System.out.println("Init impl in B");
			System.out.println(j);
		}
		
		public static class C extends B {
			
			public C(int i) {
				super(i * 31);
			}
			
		}
		
	}
	
	public static void simpleProcess() throws Exception {
		new ProcessBuilder("./assets/run.bat").start();
	}
	
	public static void directoryFiles() {
		for (File file : Objects.requireNonNull(Paths.get("./assets").toFile().listFiles())) {
			System.out.println(file.getName());
		}
	}
	
	public static void switchYield() {
		DayOfWeek day = DayOfWeek.THURSDAY;
		int j = switch (day) {
			case MONDAY -> 0;
			case TUESDAY -> 1;
			default -> day.toString().length() * 35;
		};
		System.out.println(j);
	}
	
	public static void subList() {
		List<Integer> list = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
		System.out.println(list.subList(0, 4));
		System.out.println(list.subList(4, 8));
		System.out.println(list.subList(8, 12));
		System.out.println(list.subList(12, 16));
	}
	
	public static boolean sameValues(Number... numbers) {
		if (numbers.length == 0) {
			return false;
		} else if (numbers.length == 1) {
			return true;
		}
		Number number = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (!number.equals(numbers[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static String countToString(int count) {
		if (count == 1) {
			return "";
		} else if (1000 > count) {
			return String.valueOf(count);
		} else if (10000 > count) {
			String s = "." + String.valueOf(count).charAt(1);
			if (s.equals(".0")) {
				return count / 1000 + "k";
			}
			return count / 1000 + s + "k";
		} else if (1000000 > count) {
			return count / 1000 + "k";
		}
		return "+999k";
	}
	
	public static String generatePassword(int length) {
		Random rng = new Random();
		String characters = "~_+-!#$%&0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < length; i++) {
			password.append(characters.charAt(rng.nextInt(characters.length())));
		}
		return password.toString();
	}
	
	public static UUID getUUID(String name, String password) {
		Random rng = new Random((long) name.charAt(0) * password.charAt(0));
		long mostBits = 0;
		String most = name + password + name;
		for (int i = 0; i < most.length(); i++) {
			int bound = Math.max(1, i * i) * Math.max(1, i * i);
			mostBits += (long) most.charAt(i) * rng.nextInt(bound) * rng.nextInt(bound);
		}
		System.out.println("mostBits: " + mostBits);
		long leastBits = 0;
		String least = password + name + password;
		for (int i = 0; i < least.length(); i++) {
			int bound = Math.max(1, i * i) * Math.max(1, i * i);
			leastBits += (long) least.charAt(i) * rng.nextInt(bound) * rng.nextInt(bound);
		}
		System.out.println("leastBits: " + leastBits);
		return new UUID(mostBits + rng.nextLong(leastBits), leastBits + rng.nextLong(mostBits));
	}
	
	public static int randomInt(Random rng, int min, int max) {
		return min + rng.nextInt(max - min);
	}
	
	public static int randomExclusiveInt(Random rng, int min, int max) {
		return min + rng.nextInt(max - min - 1) + 1;
	}
	
	public static int randomInclusiveInt(Random rng, int min, int max) {
		return min + rng.nextInt(max - min + 1);
	}
	
	public static double randomDouble(Random rng, double min, double max) {
		return min + rng.nextDouble(max - min);
	}
	
	public static void vanillaUUID() {
		System.out.println(UUID.randomUUID().toString().toUpperCase());
	}
	
	public static void date() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		System.out.println(format.format(new Date(System.currentTimeMillis())));
	}
	
	public static void zip() throws IOException {
		FileInputStream in = new FileInputStream("./assets/data.txt");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream("./assets/data.gz"));
		out.putNextEntry(new ZipEntry("zippedjava.txt"));
		byte[] b = new byte[1024];
		int count;
		while ((count = in.read(b)) > 0) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();
	}
	
	public static void dataFormat() {
		System.out.println(new SimpleDateFormat("ss").format(new Date(System.currentTimeMillis())));
	}
	
	public static void tickTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Hallo");
			}
		}, 0, 50);
	}
	
	public static void charInRange() {
		char c = 'r';
		if (!('z' >= c && c >= 'a' || '9' >= c && c >= '0' || c == '_')) {
			System.out.println("fd");
		}
	}
	
	public static void writeData() throws IOException {
		Path path = new File("./assets/data.txt").toPath();
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path.toFile())));
		output.writeBoolean(true);
		output.writeInt(10);
		output.writeUTF("Test");
		output.writeFloat(0.00000454F);
		output.writeBoolean(false);
		output.flush();
		output.close();
	}
	
	public static void readData() throws IOException {
		Path path = new File("./assets/data.txt").toPath();
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(path.toFile())));
		System.out.println(input.readBoolean());
		System.out.println(input.readInt());
		System.out.println(input.readUTF());
		System.out.println(input.readFloat());
		System.out.println(input.readBoolean());
		input.close();
	}
	
	public static void crypt() {
		String text = "a1$-$c3-b2$-$d4";
		long key = key("a1$-$c3-b2$-$d4");
		String encrypt = encryptString(key, text);
		System.out.println(encrypt);
		System.out.println(decryptString(key, encrypt));
	}
	
	public static String encryptString(long key, String text) {
		byte[] encryptBytes = new byte[text.getBytes().length];
		for (int i = 0; i < encryptBytes.length; i++) {
			encryptBytes[i] = (byte) (text.getBytes()[i] + key);
		}
		return new String(encryptBytes);
	}
	
	public static String decryptString(long key, String text) {
		byte[] decryptBytes = new byte[text.getBytes().length];
		for (int i = 0; i < decryptBytes.length; i++) {
			decryptBytes[i] = (byte) (text.getBytes()[i] - key);
		}
		return new String(decryptBytes);
	}
	
	public static long key(String key) {
		long l = 0;
		for (byte b : key.getBytes()) {
			l += sum(sum(b));
		}
		return l;
	}
	
	public static byte sum(byte b) {
		String s = String.valueOf(b);
		byte sum = 0;
		for (int j = 0; j < s.length(); j++) {
			sum += Integer.parseInt("" + s.charAt(j));
		}
		return sum;
	}
	
	public static long sum(long l) {
		String s = String.valueOf(l);
		long sum = 0;
		for (int j = 0; j < s.length(); j++) {
			sum += Long.parseLong("" + s.charAt(j));
		}
		return sum;
	}
	
	public static String decryptString(String string) {
		// System.out.println(Arrays.toString("binary".getBytes()));
		// System.out.println(new String(new byte[] {98, 105, 110, 97, 114, 121}));
		// System.out.println(decryptString("[98, 105, 110, 97, 114, 121]"));
		String[] strings = string.replace("[", "").replace("]", "").trim().split(", ");
		if (strings.length != 0) {
			List<Byte> byteList = new ArrayList<>();
			for (String s : strings) {
				byteList.add(Byte.valueOf(s));
			}
			byte[] bytes = new byte[byteList.size()];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = byteList.get(i);
			}
			return new String(bytes);
		}
		return "";
	}
	
	public static void streamFilter() {
		System.out.println(IntStream.range(0, 10).boxed().filter(i -> i % 2 == 0).collect(Collectors.toList()));
	}
	
	public static double lerp(double ticks, double current, double previous) {
		return current + ticks * (previous - current);
	}
	
	public static void intToRGB() {
		int r = 61;
		int g = 21;
		int b = 150;
		int rgb = r * 65536 + g * 256 + b;
		System.out.println(rgb);
		int r1 = rgb / 65536;
		System.out.println(r1);
		int g1 = (rgb - r1 * 65536) / 256;
		System.out.println(g1);
		int b1 = rgb - r1 * 65536 - g1 * 256;
		System.out.println(b1);
	}
	
	public static int getShift(int i) {
		int shift = 0;
		if (i % 2 != 0) {
			throw new IllegalArgumentException();
		}
		while (i > 1) {
			shift++;
			i /= 2;
		}
		return shift;
	}
	
	public static float floor(float f0, float g0) {
		float f1 = Math.round(f0 * 100);
		int f2 = (int) f1;
		float g1 = g0 * 100;
		int g2 = (int) g1;
		float h1 = f2 + g2;
		float h2 = h1 / 100;
		if (f0 == h2) {
			throw new IllegalArgumentException("The input value {" + f0 + "} equals the output value {" + h2 + "}");
		}
		return h2;
	}
	
	public static void createRecipeIds() {
		try {
			String path = "./assets/recipeIds.txt";
			File file = new File(path);
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < 50; i++) {
				writer.write(UUID.randomUUID() + "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double ticksToMinutes(double ticks) {
		return (ticks / 20) / 60;
	}
	
	public static double minutesToTicks(double minutes) {
		return (minutes * 60) * 20;
	}
	
	public static <T> T make(T object, Consumer<T> consumer) {
		consumer.accept(object);
		return object;
	}
}
