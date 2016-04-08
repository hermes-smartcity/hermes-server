package es.udc.lbd.hermes.model.dataservice;

public enum Service {

	SMARTDRIVER, SMARTCITIZEN;

	public String getName(){		
		return this.name();
	}

	public static Service getTipo(String tipo){
		switch (tipo){

		case "smartDriver":
			return SMARTDRIVER;
			
		case "smartCitizen":
			return SMARTCITIZEN;
		
		default:
			return null;
		}
	}
}
