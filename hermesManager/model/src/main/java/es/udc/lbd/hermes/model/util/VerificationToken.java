package es.udc.lbd.hermes.model.util;

import java.util.Calendar;
import java.util.StringTokenizer;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/* Clase que representa un token de verificación válido para confirmaciones de registro y cambio de contraseña */
public class VerificationToken {

	private String email;
	private Calendar fechaExpiracion;
	private String codigo;

	public VerificationToken(String email, long tiempoExpiracionMilisegundos) {

		this.email = email;
		this.fechaExpiracion = calcularFechaExpiracion(tiempoExpiracionMilisegundos);
		this.codigo = textEncryptor.encrypt((email + "," + this.fechaExpiracion.getTimeInMillis()));
	}

	public VerificationToken(String codigo) {

		this.codigo = codigo;

		String codigoSinCifrar = textEncryptor.decrypt(codigo);

		StringTokenizer st = new StringTokenizer(codigoSinCifrar, ",");

		this.email = st.nextToken();

		Calendar fechaExpiracion = Calendar.getInstance();
		fechaExpiracion.setTimeInMillis(Long.parseLong(st.nextToken()));
		this.fechaExpiracion = fechaExpiracion;

	}

	public String getEmail() {
		return email;
	}

	public Calendar getFechaExpiracion() {
		return fechaExpiracion;
	}

	public String getCodigo() {
		return codigo;
	}

	private Calendar calcularFechaExpiracion(long tiempoExpiracionMilisegundos) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cal.getTimeInMillis() + tiempoExpiracionMilisegundos);
		return cal;
	}

	/* Inicialización del cifrador al cargar la clase */

	private static final String PASSWORD = "987ghu9h8hijmhb";
	private static StandardPBEStringEncryptor textEncryptor;

	static {
		StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
		textEncryptor.setPassword(PASSWORD);
		textEncryptor.setAlgorithm("PBEWithMD5AndDES");
		StandardPBEStringEncryptor standardPBEStringEncryptor = (StandardPBEStringEncryptor) textEncryptor;
		standardPBEStringEncryptor.setStringOutputType("hexadecimal");
		VerificationToken.textEncryptor = textEncryptor;
	}

}
