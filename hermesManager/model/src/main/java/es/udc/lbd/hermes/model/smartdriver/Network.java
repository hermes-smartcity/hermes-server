package es.udc.lbd.hermes.model.smartdriver;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.LineString;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "network_id_seq")
@Table(name = "network")
@SuppressWarnings("serial")
public class Network implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long osm_id;
	
	private Double kmh;
	private String osm_name;
	private Integer clazz;
	private LineString geom_way;
	
	public Network(){}

	public Network(Long osm_id, Double kmh, String osm_name, Integer clazz,
			LineString geom_way) {
		super();
		this.osm_id = osm_id;
		this.kmh = kmh;
		this.osm_name = osm_name;
		this.clazz = clazz;
		this.geom_way = geom_way;
	}



	public Long getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(Long osm_id) {
		this.osm_id = osm_id;
	}

	public Double getKmh() {
		return kmh;
	}

	public void setKmh(Double kmh) {
		this.kmh = kmh;
	}

	public String getOsm_name() {
		return osm_name;
	}

	public void setOsm_name(String osm_name) {
		this.osm_name = osm_name;
	}

	public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public LineString getGeom_way() {
		return geom_way;
	}

	public void setGeom_way(LineString geom_way) {
		this.geom_way = geom_way;
	}

	
}
