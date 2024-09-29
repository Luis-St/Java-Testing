package net.luis;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author Luis-st
 *
 */

public class Cryption {
	
	public static String encrypt(String input) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(input.getBytes());
		byte[] combinedBytes = new byte[encryptedBytes.length + secretKey.getEncoded().length];
		System.arraycopy(encryptedBytes, 0, combinedBytes, 0, encryptedBytes.length);
		System.arraycopy(secretKey.getEncoded(), 0, combinedBytes, encryptedBytes.length, secretKey.getEncoded().length);
		return Base64.getEncoder().encodeToString(combinedBytes);
	}
	
	public static String decrypt(String input) throws Exception {
		byte[] combinedBytes = Base64.getDecoder().decode(input);
		byte[] encryptedBytes = new byte[combinedBytes.length - 32];
		byte[] keyBytes = new byte[32];
		System.arraycopy(combinedBytes, 0, encryptedBytes, 0, encryptedBytes.length);
		System.arraycopy(combinedBytes, encryptedBytes.length, keyBytes, 0, keyBytes.length);
		SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		return new String(decryptedBytes);
	}
	
}
