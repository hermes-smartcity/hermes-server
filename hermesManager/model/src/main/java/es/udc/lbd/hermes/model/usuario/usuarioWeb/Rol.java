package es.udc.lbd.hermes.model.usuario.usuarioWeb;

public enum Rol {

	CONSULTA, ADMIN;

	public String getName(){		
		return this.name();
	}

	public static Rol getTipo(String tipo){
		switch (tipo){
		case "Consulta":
			return CONSULTA;
		case "Admin":
			return ADMIN;
		default:
			return null;
		}
	}
}

