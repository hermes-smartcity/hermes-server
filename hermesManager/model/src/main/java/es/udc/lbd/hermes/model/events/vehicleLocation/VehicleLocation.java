package es.udc.lbd.hermes.model.events.vehicleLocation;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
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

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;


@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "vehicleLocation_id_seq")
@Table(name = "vehicleLocation")
@SuppressWarnings("serial")
public class VehicleLocation implements Serializable{

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

		private Double accuracy;
		
		private Double speed;
		
		private Double rr;
		
		@ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuarioMovil")
    	private UsuarioMovil usuarioMovil;
        
        public VehicleLocation() {
        }

        public VehicleLocation(Calendar timestamp, String eventId, Point position) {
        	this.timestamp= timestamp;
        	this.eventId= eventId;
        	this.position= position;
        }
        
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
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
		public Double getSpeed() {
			return speed;
		}

		public void setSpeed(Double speed) {
			this.speed = speed;
		}

		public Double getRr() {
			return rr;
		}

		public void setRr(Double rr) {
			this.rr = rr;
		}
		
		
}