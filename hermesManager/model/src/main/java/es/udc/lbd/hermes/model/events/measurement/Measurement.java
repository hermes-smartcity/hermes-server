package es.udc.lbd.hermes.model.events.measurement;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;

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
        
		@Enumerated(EnumType.STRING)
        private MeasurementType tipo;
        
        private Double value;
        
        private Double accuracy;

        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuarioMovil")
    	private UsuarioMovil usuarioMovil;
        
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

		public MeasurementType getTipo() {
			return tipo;
		}

		public void setTipo(MeasurementType tipo) {
			this.tipo = tipo;
		}

		public Double getValue() {
			return value;
		}

		public void setValue(Double value) {
			this.value = value;
		}

		public UsuarioMovil getUsuarioMovil() {
			return usuarioMovil;
		}

		public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
			this.usuarioMovil = usuarioMovil;
		}

		public Double getAccuracy() {
			return accuracy;
		}

		public void setAccuracy(Double accuracy) {
			this.accuracy = accuracy;
		}
		
}