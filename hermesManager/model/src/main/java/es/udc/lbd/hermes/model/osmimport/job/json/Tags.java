package es.udc.lbd.hermes.model.osmimport.job.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tags {

	private Map<String, String> atributos = new HashMap<String, String>();

	public void crearAtributo(String atributo, String value){
		atributos.put(atributo, value);
	}

	public String recuperarAtributoIndicado(String atributo){
		return atributos.get(atributo);
	}
}
