package es.udc.lbd.hermes.model.usuario.usuarioWeb;


public class PasswordJSON{

	private String passwordOld;
	private String passwordNew1;
	private String passwordNew2;

	public PasswordJSON() {}

	public PasswordJSON(String passwordOld, String passwordNew1, String passwordNew2)
	{
		this.passwordOld = passwordOld;
		this.passwordNew1 = passwordNew1;
		this.passwordNew2 = passwordNew2;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public String getPasswordNew1() {
		return passwordNew1;
	}

	public void setPasswordNew1(String passwordNew1) {
		this.passwordNew1 = passwordNew1;
	}

	public String getPasswordNew2() {
		return passwordNew2;
	}

	public void setPasswordNew2(String passwordNew2) {
		this.passwordNew2 = passwordNew2;
	}


}