package es.udc.lbd.hermes.model.util;

import java.text.Normalizer;

public class UserUtils {

	/** Elimina los caracteres no unicode del login y lo pasa a minúsculas **/
	public static String normalizarLogin(String login) {
		String loginNormalizado = Normalizer.normalize(login.toLowerCase(), Normalizer.Form.NFD); 
		return loginNormalizado.replaceAll("\\p{M}", "");
	}
	
}
