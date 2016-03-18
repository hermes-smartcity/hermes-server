package es.udc.lbd.hermes.model.events.log;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "logs_id_seq")
@Table(name = "logs")
@SuppressWarnings("serial")
public class Log implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		@Temporal(TemporalType.TIMESTAMP)
		private Calendar dated;
        
		private String logger;
		private String level;
		private String message;
	
        public Log() {}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Calendar getDated() {
			return dated;
		}

		public void setDated(Calendar dated) {
			this.dated = dated;
		}

		public String getLogger() {
			return logger;
		}

		public void setLogger(String logger) {
			this.logger = logger;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

       
}