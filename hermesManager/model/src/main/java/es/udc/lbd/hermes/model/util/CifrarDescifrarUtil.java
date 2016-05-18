package es.udc.lbd.hermes.model.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CifrarDescifrarUtil {

	public static String cifra(String sinCifrar) throws Exception {
		final byte[] bytes = sinCifrar.getBytes("UTF-8");
		final Cipher aes = obtieneCipher(true);
		final byte[] cifrado = aes.doFinal(bytes);
		
		byte[] encoded = Base64.encodeBase64(cifrado);
		
		return new String(encoded);
	}

	public static String descifra(String conCifrado) throws Exception {
		byte[] cifrado = Base64.decodeBase64(conCifrado);
		final Cipher aes = obtieneCipher(false);
		final byte[] bytes = aes.doFinal(cifrado);
		final String sinCifrar = new String(bytes, "UTF-8");
		return sinCifrar;
	}
	
	
	private static Cipher obtieneCipher(boolean paraCifrar) throws Exception {
		//final String frase = "FraseLargaConDiferentesLetrasNumerosYCaracteresEspeciales_áÁéÉíÍóÓúÚüÜñÑ1234567890!#%$&()=%_NO_USAR_ESTA_FRASE!_";
		final String frase = "!!FrAsePaRaEnCrIpTaCiOn_#()#_%45589239087%_PrOyEcTo!!";
		final MessageDigest digest = MessageDigest.getInstance("SHA");
		digest.update(frase.getBytes(StandardCharsets.UTF_8));
		final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

		final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		if (paraCifrar) {
			aes.init(Cipher.ENCRYPT_MODE, key);
		} else {
			aes.init(Cipher.DECRYPT_MODE, key);
		}

		return aes;
	}
	
	
}
