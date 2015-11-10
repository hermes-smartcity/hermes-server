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

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;

import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiLineStringDeserializer;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "driverFeatures_id_seq")
@Table(name = "driverFeatures")
@SuppressWarnings("serial")
public class DriverFeatures implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
        
        private int awakefor;
        
        private int inBed ;
        
        private int workingTime;

        private int deepSleep;
        
        
        @ManyToOne(fetch = FetchType.EAGER)
    	@JoinColumn(name = "idUsuario")
    	private Usuario usuario;
        
        public DriverFeatures() {
        }

        public DriverFeatures(int awakefor, int inBed, int workingTime, int deepSleep) {
        	this.awakefor= awakefor;
        	this.inBed= inBed;
        	this.workingTime= workingTime;
        	this.deepSleep= deepSleep;
        }
       
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getAwakefor() {
			return awakefor;
		}

		public void setAwakefor(int awakefor) {
			this.awakefor = awakefor;
		}

		public int getInBed() {
			return inBed;
		}

		public void setInBed(int inBed) {
			this.inBed = inBed;
		}

		public int getWorkingTime() {
			return workingTime;
		}

		public void setWorkingTime(int workingTime) {
			this.workingTime = workingTime;
		}

		public int getDeepSleep() {
			return deepSleep;
		}

		public void setDeepSleep(int deepSleep) {
			this.deepSleep = deepSleep;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
		
}