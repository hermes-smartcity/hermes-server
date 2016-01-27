package es.udc.lbd.hermes.model.usuario.usuarioWeb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;


@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "usuario_web_id_seq")
@Table(name = "usuario_web")
@SuppressWarnings("serial")
public class UsuarioWeb implements UserDetails {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
		
		private String email;
		
		private String password;
		
		private boolean activado;
		
		@Enumerated(EnumType.STRING)
        private Rol rol;
			
		@OneToOne (fetch=FetchType.LAZY)	
		@JoinColumn(name = "id_usuario_movil")
		private UsuarioMovil usuarioMovil;
		
        public UsuarioWeb() {
        }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Rol getRol() {
			return rol;
		}

		public void setRol(Rol rol) {
			this.rol = rol;
		}

		public UsuarioMovil getUsuarioMovil() {
			return usuarioMovil;
		}

		public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
			this.usuarioMovil = usuarioMovil;
		}
	
		@Override
		@Transient
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(this.getRol().toString()));
			return authorities;
		}
		
		@Override
		@Transient
		public String getUsername() {
			return email;
		}

		@Override
		@Transient
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		@Transient
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		@Transient
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		@Transient
		public boolean isEnabled() {
			return activado;
		}

}