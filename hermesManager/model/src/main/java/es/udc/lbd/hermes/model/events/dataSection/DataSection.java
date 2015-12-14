package es.udc.lbd.hermes.model.events.dataSection;

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
import com.vividsolutions.jts.geom.LineString;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiLineStringDeserializer;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "dataSection_id_seq")
@Table(name = "dataSection")
@SuppressWarnings("serial")
public class DataSection implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		@Temporal(TemporalType.TIMESTAMP)
		private Calendar timestamp;
        
		private String eventId;
		
		private Double minSpeed;
		
		private Double maxSpeed;
		
		private Double medianSpeed;
		
		private Double averageSpeed;
		
		private Double averageRR;
		
		private Double averageHeartRate;
		
		private Double standardDeviationSpeed;
		
		private Double standardDeviationRR;
		
		private Double standardDeviationHeartRate;
		
		private Double pke;
        
        @Type(type="org.hibernate.spatial.GeometryType")
        @JsonSerialize(using = CustomGeometrySerializer.class)
        @JsonDeserialize(using = CustomMultiLineStringDeserializer.class)       
        private LineString roadSection;
        
        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuarioMovil")
    	private UsuarioMovil usuarioMovil;
        
        public DataSection() {
        }

        public DataSection(Calendar timestamp, String eventId) {
        	this.timestamp= timestamp;
        	this.eventId= eventId;
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

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}
		
		public Double getMinSpeed() {
			return minSpeed;
		}

		public void setMinSpeed(Double minSpeed) {
			this.minSpeed = minSpeed;
		}

		public Double getMaxSpeed() {
			return maxSpeed;
		}

		public void setMaxSpeed(Double maxSpeed) {
			this.maxSpeed = maxSpeed;
		}

		public Double getMedianSpeed() {
			return medianSpeed;
		}

		public void setMedianSpeed(Double medianSpeed) {
			this.medianSpeed = medianSpeed;
		}

		public Double getAverageSpeed() {
			return averageSpeed;
		}

		public void setAverageSpeed(Double averageSpeed) {
			this.averageSpeed = averageSpeed;
		}

		public Double getAverageRR() {
			return averageRR;
		}

		public void setAverageRR(Double averageRR) {
			this.averageRR = averageRR;
		}

		public Double getAverageHeartRate() {
			return averageHeartRate;
		}

		public void setAverageHeartRate(Double averageHeartRate) {
			this.averageHeartRate = averageHeartRate;
		}

		public Double getStandardDeviationSpeed() {
			return standardDeviationSpeed;
		}

		public void setStandardDeviationSpeed(Double standardDeviationSpeed) {
			this.standardDeviationSpeed = standardDeviationSpeed;
		}

		public Double getStandardDeviationRR() {
			return standardDeviationRR;
		}

		public void setStandardDeviationRR(Double standardDeviationRR) {
			this.standardDeviationRR = standardDeviationRR;
		}

		public Double getStandardDeviationHeartRate() {
			return standardDeviationHeartRate;
		}

		public void setStandardDeviationHeartRate(Double standardDeviationHeartRate) {
			this.standardDeviationHeartRate = standardDeviationHeartRate;
		}

		public Double getPke() {
			return pke;
		}

		public void setPke(Double pke) {
			this.pke = pke;
		}

		public LineString getRoadSection() {
			return roadSection;
		}

		public void setRoadSection(LineString roadSection) {
			this.roadSection = roadSection;
		}

		public UsuarioMovil getUsuarioMovil() {
			return usuarioMovil;
		}

		public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
			this.usuarioMovil = usuarioMovil;
		}
	
}