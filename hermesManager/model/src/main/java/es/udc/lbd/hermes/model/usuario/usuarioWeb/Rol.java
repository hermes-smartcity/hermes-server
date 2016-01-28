package es.udc.lbd.hermes.model.usuario.usuarioWeb;

public enum Rol {

	ROLE_CONSULTA, ROLE_ADMIN;

	public String getName(){		
		return this.name();
	}

	public static Rol getTipo(String tipo){
		switch (tipo){
		case "Role_Consulta":
			return ROLE_CONSULTA;
		case "Role_Admin":
			return ROLE_ADMIN;
		default:
			return null;
		}
	}
}

