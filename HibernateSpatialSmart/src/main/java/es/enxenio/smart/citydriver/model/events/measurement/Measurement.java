package es.enxenio.smart.citydriver.model.events.measurement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.enxenio.smart.citydriver.model.usuario.Usuario;
import es.enxenio.smart.citydriver.model.util.jackson.CustomGeometrySerializer;
import es.enxenio.smart.citydriver.model.util.jackson.CustomMultiLineStringDeserializer;
import es.enxenio.smart.citydriver.model.util.jackson.CustomPointDeserializer;


import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "measurement_id_seq")
@Table(name = "measurement")
@SuppressWarnings("serial")
public class Measurement implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		@Temporal(TemporalType.TIMESTAMP)
		private Calendar timestamp;
        
		private String eventId;
		
		@Type(type="org.hibernate.spatial.GeometryType")
		@JsonSerialize(using = CustomGeometrySerializer.class)
        @JsonDeserialize(using = CustomPointDeserializer.class)		
        private Point position;
        
//      @Enumerated(EnumType.STRING)
        private String tipo;
        
        private int value;

        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuario")
    	private Usuario usuario;
        
        public Measurement() {
        }

        public Long getId() {
            return id;
        }

        private void setId(Long id) {
            this.id = id;
        }

		public Calendar getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Calendar timestamp) {
			this.timestamp = timestamp;
		}

		public Point getPosition() {
			return position;
		}

		public void setPosition(Point position) {
			this.position = position;
		}

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

}