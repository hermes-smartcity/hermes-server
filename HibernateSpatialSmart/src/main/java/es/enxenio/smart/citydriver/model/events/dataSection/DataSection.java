package es.enxenio.smart.citydriver.model.events.dataSection;

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

        private int minHeartRate;
        
        private int maxBeatBeat;
        
        private int maxHeartRate;

        private Double standardDeviationSpeed;
        
        private int  minBeatBeat;
        
        private Double minSpeed;
        
        private Double averageSpeed;
        
        private Double standardDeviationBeatBeat;
        
        private Double heartRate;
        
        private Double medianSpeed;
        
        private Double standardDeviationHeartRate;
        
        private Double maxSpeed;
        
        private Double pke;
        
        @Type(type="org.hibernate.spatial.GeometryType")
        @JsonSerialize(using = CustomGeometrySerializer.class)
        @JsonDeserialize(using = CustomMultiLineStringDeserializer.class)       
        private LineString roadSection;
        
        private int medianHeartRate;
        
        private Double meanBeatBeat;
        
        private int medianBeatBeat;
        
        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuario")
    	private Usuario usuario;
        
        @Version
    	@SuppressWarnings("unused")
    	private long version;
        
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

		public int getMinHeartRate() {
			return minHeartRate;
		}

		public void setMinHeartRate(int minHeartRate) {
			this.minHeartRate = minHeartRate;
		}

		public int getMaxBeatBeat() {
			return maxBeatBeat;
		}

		public void setMaxBeatBeat(int maxBeatBeat) {
			this.maxBeatBeat = maxBeatBeat;
		}

		public int getMaxHeartRate() {
			return maxHeartRate;
		}

		public void setMaxHeartRate(int maxHeartRate) {
			this.maxHeartRate = maxHeartRate;
		}

		public Double getStandardDeviationSpeed() {
			return standardDeviationSpeed;
		}

		public void setStandardDeviationSpeed(Double standardDeviationSpeed) {
			this.standardDeviationSpeed = standardDeviationSpeed;
		}

		public int getMinBeatBeat() {
			return minBeatBeat;
		}

		public void setMinBeatBeat(int minBeatBeat) {
			this.minBeatBeat = minBeatBeat;
		}

		public Double getMinSpeed() {
			return minSpeed;
		}

		public void setMinSpeed(Double minSpeed) {
			this.minSpeed = minSpeed;
		}

		public Double getAverageSpeed() {
			return averageSpeed;
		}

		public void setAverageSpeed(Double averageSpeed) {
			this.averageSpeed = averageSpeed;
		}

		public Double getStandardDeviationBeatBeat() {
			return standardDeviationBeatBeat;
		}

		public void setStandardDeviationBeatBeat(Double standardDeviationBeatBeat) {
			this.standardDeviationBeatBeat = standardDeviationBeatBeat;
		}

		public Double getHeartRate() {
			return heartRate;
		}

		public void setHeartRate(Double heartRate) {
			this.heartRate = heartRate;
		}

		public Double getMedianSpeed() {
			return medianSpeed;
		}

		public void setMedianSpeed(Double medianSpeed) {
			this.medianSpeed = medianSpeed;
		}

		public Double getStandardDeviationHeartRate() {
			return standardDeviationHeartRate;
		}

		public void setStandardDeviationHeartRate(Double standardDeviationHeartRate) {
			this.standardDeviationHeartRate = standardDeviationHeartRate;
		}

		public Double getMaxSpeed() {
			return maxSpeed;
		}

		public void setMaxSpeed(Double maxSpeed) {
			maxSpeed = maxSpeed;
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

		public int getMedianHeartRate() {
			return medianHeartRate;
		}

		public void setMedianHeartRate(int medianHeartRate) {
			this.medianHeartRate = medianHeartRate;
		}

		public Double getMeanBeatBeat() {
			return meanBeatBeat;
		}

		public void setMeanBeatBeat(Double meanBeatBeat) {
			this.meanBeatBeat = meanBeatBeat;
		}

		public int getMedianBeatBeat() {
			return medianBeatBeat;
		}

		public void setMedianBeatBeat(int medianBeatBeat) {
			this.medianBeatBeat = medianBeatBeat;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public long getVersion() {
			return version;
		}

		public void setVersion(long version) {
			this.version = version;
		}
		
}