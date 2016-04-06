package es.udc.lbd.hermes.model.smartdriver;

public enum Method {

	GET_INFORMATION_LINK;

	public String getName(){		
		return this.name();
	}

	public static Method getTipo(String tipo){
		switch (tipo){
		case "getLinkInformation":
			return GET_INFORMATION_LINK;
		
		default:
			return null;
		}
	}
}
