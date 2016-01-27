package es.udc.lbd.hermes.model.auth;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;

@SuppressWarnings("serial")
public class UserAuthentication implements Authentication {

	private final UsuarioWeb usuario;
	private boolean tokenLargaDuracion;
	private boolean authenticated = true;

	public UserAuthentication(UsuarioWeb usuario, boolean tokenLargaDuracion) {
		this.usuario = usuario;
		this.tokenLargaDuracion= tokenLargaDuracion;
	}
	
	public UserAuthentication(UsuarioWeb usuario) {
		this.usuario = usuario;
	}

	@Override
	public String getName() {
		return usuario.getEmail();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return usuario.getPassword();
	}

	@Override
	public UsuarioWeb getDetails() {
		return usuario;
	}

	@Override
	public Object getPrincipal() {
		return usuario.getEmail();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public void setTokenLargaDuracion(boolean tokenLargaDuracion) {
		this.tokenLargaDuracion = tokenLargaDuracion;
	}

	public boolean isTokenLargaDuracion() {
		return tokenLargaDuracion;
	}

}
