package es.udc.lbd.hermes.model.auth.dto;

public class TokenRefreshDTO {

	private String username;
	private boolean tokenLargaDuracion;
	private Long fechaEmision;
	private Long fechaExpiracion;

	public TokenRefreshDTO(String username, boolean tokenLargaDuracion, Long fechaEmision, Long fechaExpiracion) {
		this.username = username;
		this.tokenLargaDuracion = tokenLargaDuracion;
		this.fechaEmision = fechaEmision;
		this.fechaExpiracion = fechaExpiracion;
	}

	public TokenRefreshDTO() {
	}

	public boolean isTokenLargaDuracion() {
		return tokenLargaDuracion;
	}

	public void setTokenLargaDuracion(boolean tokenLargaDuracion) {
		this.tokenLargaDuracion = tokenLargaDuracion;
	}

	public Long getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Long fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Long getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Long fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
