package es.udc.lbd.hermes.model.events.sleepData;

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

import es.udc.lbd.hermes.model.usuario.Usuario;


@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "sleepdata_id_seq")
@Table(name = "sleepdata")
@SuppressWarnings("serial")
public class SleepData implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
		private String eventId;

		private Integer awakenings;
		private Integer minutesAsleep;
		private Integer minutesInBed;
		@Temporal(TemporalType.TIMESTAMP)
		private Calendar startTime;
		@Temporal(TemporalType.TIMESTAMP)
		private Calendar endTime;
		
		@ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuario")
    	private Usuario usuario;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public Integer getAwakenings() {
			return awakenings;
		}

		public void setAwakenings(Integer awakenings) {
			this.awakenings = awakenings;
		}

		public Integer getMinutesAsleep() {
			return minutesAsleep;
		}

		public void setMinutesAsleep(Integer minutesAsleep) {
			this.minutesAsleep = minutesAsleep;
		}

		public Integer getMinutesInBed() {
			return minutesInBed;
		}

		public void setMinutesInBed(Integer minutesInBed) {
			this.minutesInBed = minutesInBed;
		}

		public Calendar getStartTime() {
			return startTime;
		}

		public void setStartTime(Calendar startTime) {
			this.startTime = startTime;
		}

		public Calendar getEndTime() {
			return endTime;
		}

		public void setEndTime(Calendar endTime) {
			this.endTime = endTime;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
}