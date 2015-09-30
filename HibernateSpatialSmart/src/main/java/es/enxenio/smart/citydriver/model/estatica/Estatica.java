package es.enxenio.smart.citydriver.model.estatica;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;

import es.enxenio.smart.citydriver.model.usuario.Usuario;
import es.enxenio.smart.citydriver.model.util.jackson.CustomGeometrySerializer;
import es.enxenio.smart.citydriver.model.util.jackson.CustomMultiLineStringDeserializer;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "estatica_id_seq")
@Table(name = "estatica")
@SuppressWarnings("serial")
public class Estatica implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
      
		private String titulo;
		
		private String contenido;

        @Version
    	@SuppressWarnings("unused")
    	private long version;
        
        public Estatica() {
        }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public String getContenido() {
			return contenido;
		}

		public void setContenido(String contenido) {
			this.contenido = contenido;
		}

		public long getVersion() {
			return version;
		}

		public void setVersion(long version) {
			this.version = version;
		}

}