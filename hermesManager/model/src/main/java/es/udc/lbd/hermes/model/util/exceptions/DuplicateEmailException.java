package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class DuplicateEmailException extends Exception {

	private String email;

	public DuplicateEmailException(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setLogin(String email) {
		this.email = email;
	}

}
