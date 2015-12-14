package es.udc.lbd.hermes.model.usuario.usuarioMovil;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "usuario_movil_id_seq")
@Table(name = "usuario_movil")
@SuppressWarnings("serial")
public class UsuarioMovil implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		private String sourceId;		
		        
        public UsuarioMovil() {
        }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSourceId() {
			return sourceId;
		}

		public void setSourceId(String sourceId) {
			this.sourceId = sourceId;
		}

}