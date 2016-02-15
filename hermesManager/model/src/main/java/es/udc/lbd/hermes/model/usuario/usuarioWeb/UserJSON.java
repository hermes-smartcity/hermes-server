package es.udc.lbd.hermes.model.usuario.usuarioWeb;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;

public class UserJSON
{

	private String email;
	private String password;
	private Rol rol;
	// Unicamente para la funcionalidad de editar id_usuario_movil
	private String sourceIdUsuarioMovilNuevo;
	
   public UserJSON() {
   }

	public UserJSON(String email, String password, Rol rol)
	{
		this.email = email;
		this.password = password;
		this.rol = rol;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Rol getRol() {
		return rol;
	}

	public String getSourceIdUsuarioMovilNuevo() {
		return sourceIdUsuarioMovilNuevo;
	}

	public void setSourceIdUsuarioMovilNuevo(String sourceIdUsuarioMovilNuevo) {
		this.sourceIdUsuarioMovilNuevo = sourceIdUsuarioMovilNuevo;
	}
	
}