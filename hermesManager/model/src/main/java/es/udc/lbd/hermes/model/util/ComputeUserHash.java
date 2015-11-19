package es.udc.lbd.hermes.model.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class ComputeUserHash {

	public static void main(String[] args) {
		String cadena = "luaces@lbd.org.es";
		String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println(hash);
	}
}
