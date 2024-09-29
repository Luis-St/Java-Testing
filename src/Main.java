import java.math.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		
	}
	
	private static BigDecimal pow(BigDecimal base, BigDecimal exponent, MathContext mc) {
		BigDecimal result = BigDecimal.ONE;
		for (BigDecimal i = BigDecimal.ZERO; i.compareTo(exponent) < 0; i = i.add(BigDecimal.ONE)) {
			result = result.multiply(base, mc);
		}
		return result;
	}
}
