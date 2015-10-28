package es.enxenio.smart.model.usuario;

import com.vividsolutions.jts.geom.Point;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "usuario_id_seq")
@Table(name = "usuario")
@SuppressWarnings("serial")
public class Usuario implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		private String sourceId;
		
		        
        public Usuario() {
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