package es.udc.lbd.hermes.model.osmimport.job.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag {

	@JsonProperty("VRS:gemeinde")
	private String vrsGemeinde;
	
	@JsonProperty("VRS:ortsteil")
	private String vrsOrtsteil;
	
	@JsonProperty("VRS:ref")
	private String vrsRef;
	
	@JsonProperty("bus")
	private String bus;
	
	@JsonProperty("highway")
	private String highway;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("operator")
	private String operator;
	
	@JsonProperty("public_transport")
	private String publicTransport;

	public String getVrsGemeinde() {
		return vrsGemeinde;
	}

	public void setVrsGemeinde(String vrsGemeinde) {
		this.vrsGemeinde = vrsGemeinde;
	}

	public String getVrsOrtsteil() {
		return vrsOrtsteil;
	}

	public void setVrsOrtsteil(String vrsOrtsteil) {
		this.vrsOrtsteil = vrsOrtsteil;
	}

	public String getVrsRef() {
		return vrsRef;
	}

	public void setVrsRef(String vrsRef) {
		this.vrsRef = vrsRef;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public String getHighway() {
		return highway;
	}

	public void setHighway(String highway) {
		this.highway = highway;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPublicTransport() {
		return publicTransport;
	}

	public void setPublicTransport(String publicTransport) {
		this.publicTransport = publicTransport;
	}
	
	public String recuperarAtributoIndicado(String atributo){
		String resultado = null;
		switch (atributo) {
		case "VRS:gemeinde":
			resultado = getVrsGemeinde();
			break;

		case "VRS:ortsteil":
			resultado = getVrsOrtsteil();	
			break;
		case "VRS:ref":
			resultado = getVrsRef();
			break;
		case "bus":
			resultado = getBus();
			break;
		case "highway":
			resultado = getHighway();
			break;
		case "name":
			resultado = getName();
			break;
		case "operator":
			resultado = getOperator();
			break;
		case "public_transport":
			resultado = getPublicTransport();
			break;
		
		}
		
		return resultado;
	}
}
