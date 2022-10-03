package ad.smartstudio.caea.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class EncryptionUtils {
	private final static Log LOG = LogFactory.getLog("EncryptUtil");
	protected final static String deskey = "3#s<KT;9mB@";
	protected final static String desedekey = "&;k(Qs`Mo8{g;JUb!N44QWH+";
	private static String charsetName = "UTF8";
	private static String DESAlgorithm = "DES";
	private static String DESedeAlgorithm = "DESede";

	public static String getDesKey() {
		return deskey;
	}

	public static String getDesedeKey() {
		return desedekey;
	}

	public static String encryptDES(String data) {
		if (data == null) {
			return null;
		}
		try {
			DESKeySpec desKeySpec = new DESKeySpec(deskey.getBytes(charsetName));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DESAlgorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			byte[] dataBytes = data.getBytes(charsetName);
			Cipher cipher = Cipher.getInstance(DESAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] dataBytesEncrypted = cipher.doFinal(dataBytes);
			return new String(Base64.getEncoder().encode(dataBytesEncrypted));
		} catch (Exception e) {
			return null;
		}
	}

	public static String decryptDES(String data) throws Exception {
		if (data == null) {
			return null;
		}
		try {
			byte[] dataBytes = Base64.getDecoder().decode(data);
			DESKeySpec desKeySpec = new DESKeySpec(deskey.getBytes(charsetName));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DESAlgorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance(DESAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
			return new String(dataBytesDecrypted);
		} catch (Exception e) {
			throw e;
		}
	}

	public static String encryptDESede(String data) {
		if (data == null) {
			return null;
		}
		try {
			DESedeKeySpec desKeySpec = new DESedeKeySpec(desedekey.getBytes(charsetName));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DESedeAlgorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			byte[] dataBytes = data.getBytes(charsetName);
			Cipher cipher = Cipher.getInstance(DESedeAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] dataBytesEncrypted = cipher.doFinal(dataBytes);
			return new String(Base64.getEncoder().encode(dataBytesEncrypted));
		} catch (Exception e) {
			return null;
		}
	}

	public static String decryptDESede(String data) throws Exception {
		if (data == null) {
			return null;
		}
		try {
			byte[] dataBytes = Base64.getDecoder().decode(data);
			DESedeKeySpec desKeySpec = new DESedeKeySpec(desedekey.getBytes(charsetName));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DESedeAlgorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance(DESedeAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
			return new String(dataBytesDecrypted);
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean isDESedeEncrypted(String data) {
		try {
			if (data.length() < 4)
				return false;
			decryptDESede(data);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isDESEncrypted(String data) {
		try {
			if (data.length() < 4)
				return false;
			decryptDES(data);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("EncryptionUtils uses:");
			System.out.println("   encrypt {string}");
			System.out.println("   decrypt {string}");
			return;
		}
		if (args[0].equalsIgnoreCase("encrypt"))
			System.out.println(EncryptionUtils.encryptDESede(args[1]));
		if (args[0].equalsIgnoreCase("decrypt"))
			System.out.println(EncryptionUtils.decryptDESede(args[1]));
	}

	public static String hash(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			LOG.debug(e);
		}
		return null;
	}
}

