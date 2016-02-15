package es.udc.lbd.hermes.model.mail.mail;

import java.util.Arrays;
import java.util.List;

public class SimpleMail {

	private String correoElectronico;
	private String asunto;
	private String texto;
	private String textoHTML;
	private String[] replyTo;
	private List<byte []> archivos;

	public SimpleMail(String correoElectronico, String asunto, String texto, String textoHTML, String[] replyTo, List<byte []> archivos) {
		this.correoElectronico = correoElectronico;
		this.asunto = asunto;
		this.texto = texto;
		this.textoHTML = textoHTML;
		this.replyTo = replyTo;
		this.archivos = archivos;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getTexto() {
		return texto;
	}

	public String getTextoHTML() {
		return textoHTML;
	}

	public String[] getReplyTo() {
		return replyTo;
	}
	
	public List<byte[]> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<byte[]> archivos) {
		this.archivos = archivos;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
			.append("SimpleMail [correoElectronico=").append(correoElectronico)
			.append(", asunto=").append(asunto)
			.append(", texto=").append(texto)
			.append(", textoHTML=").append(textoHTML)
			.append(", replyTo=").append(Arrays.toString(replyTo))
			.append("]");
		return builder.toString();
	}

}
