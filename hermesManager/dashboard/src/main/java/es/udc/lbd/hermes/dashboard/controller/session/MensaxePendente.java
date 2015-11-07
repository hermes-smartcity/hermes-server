package es.udc.lbd.hermes.dashboard.controller.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Contedor para as mensaxes destacadas que se amosaran ao usuario.
 * 
 * @author Sergio (14/10/2008)
 */
public class MensaxePendente {

	public enum TipoMensaxe {
		ERRO, AVISO, INFO, CORRECTO
	}

	private TipoMensaxe tipo;
	private String texto;
	private List<String> parametros;

	public MensaxePendente(String texto, TipoMensaxe tipo) {
		super();
		this.texto = texto;
		this.tipo = tipo;
		this.parametros = new ArrayList<String>();
	}
	
	public MensaxePendente(String texto, TipoMensaxe tipo, String ... parametros) {
		super();
		this.texto = texto;
		this.tipo = tipo;
		this.parametros = Arrays.asList(parametros);
	}

	public TipoMensaxe getTipo() {
		return tipo;
	}

	public void setTipo(TipoMensaxe tipo) {
		this.tipo = tipo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<String> getParametros() {
		return parametros;
	}

	public void setParametros(List<String> parametros) {
		this.parametros = parametros;
	}
	
	
}
