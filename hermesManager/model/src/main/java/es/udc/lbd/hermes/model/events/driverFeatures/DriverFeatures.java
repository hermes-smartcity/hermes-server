package es.udc.lbd.hermes.model.events.driverFeatures;

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

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "driverFeatures_id_seq")
@Table(name = "driverFeatures")
@SuppressWarnings("serial")
public class DriverFeatures implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
        
        private Integer awakeFor;
        
        private Integer inBed ;
        
        private Integer workingTime;

        private Integer deepSleep;
        
        private Integer previousStress;        
        
        private Integer lightSleep;
        
        @Temporal(TemporalType.TIMESTAMP)
		private Calendar timestamp;
        
        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuarioMovil")
    	private UsuarioMovil usuarioMovil;
        
        public DriverFeatures() {
        }

        public DriverFeatures(Integer awakeFor, Integer inBed, Integer workingTime, Integer deepSleep,
        		Integer lightSleep, Calendar timestamp) {
        	this.awakeFor= awakeFor;
        	this.inBed= inBed;
        	this.workingTime= workingTime;
        	this.deepSleep= deepSleep;
        	this.lightSleep= lightSleep;
        	this.timestamp = timestamp;
        }
       
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getAwakeFor() {
			return awakeFor;
		}

		public void setAwakeFor(Integer awakeFor) {
			this.awakeFor = awakeFor;
		}

		public Integer getInBed() {
			return inBed;
		}

		public void setInBed(Integer inBed) {
			this.inBed = inBed;
		}

		public Integer getWorkingTime() {
			return workingTime;
		}

		public void setWorkingTime(Integer workingTime) {
			this.workingTime = workingTime;
		}

		public Integer getDeepSleep() {
			return deepSleep;
		}

		public void setDeepSleep(Integer deepSleep) {
			this.deepSleep = deepSleep;
		}

		public Integer getLightSleep() {
			return lightSleep;
		}

		public void setLightSleep(Integer lightSleep) {
			this.lightSleep = lightSleep;
		}

		public Integer getPreviousStress() {
			return previousStress;
		}

		public void setPreviousStress(Integer previousStress) {
			this.previousStress = previousStress;
		}

		public UsuarioMovil getUsuarioMovil() {
			return usuarioMovil;
		}

		public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
			this.usuarioMovil = usuarioMovil;
		}

		public Calendar getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Calendar timestamp) {
			this.timestamp = timestamp;
		}
		
}