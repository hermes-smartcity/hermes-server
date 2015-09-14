package es.enxenio.smart.citydriver.model.events.eventoProcesado;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "eventoProcesado_id_seq")
@Table(name = "eventoProcesado")
@SuppressWarnings("serial")
public class EventoProcesado implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		@Temporal(TemporalType.TIMESTAMP)
		private Calendar timestamp;
        
		private String eventId;
		
		@Version
		@SuppressWarnings("unused")
		private long version;
       
        public EventoProcesado() {
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

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public long getVersion() {
			return version;
		}

		public void setVersion(long version) {
			this.version = version;
		}
		
}