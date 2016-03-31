package es.udc.lbd.hermes.model.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashUtil {

	public static String generarHash(String cadena){
		String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		return hash;
	}

	public static String generarHashPassword(String cadena){
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		return passwordEncoder.encode(cadena.toString());  
	}
}
