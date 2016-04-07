package es.udc.lbd.hermes.model.smartdriver;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.LineString;

@Entity
@Table(schema="\"network\"", name = "es_cor_2po_4pgr")
@SuppressWarnings("serial")
public class NetworkLink implements Serializable{

	@Id
	private Long id;
	
	private Long osm_id;
	private String osm_name;
	private String osm_meta;
	private Long osm_source_id;
	private Long osm_target_id;
	private Integer clazz;
	private Integer flags;
	private Integer source;
	private Integer target;
	private Double km;
	private Integer kmh;
	private Double cost;
	private Double reverse_cost;
	private Double x1;
	private Double y1;
	private Double x2;
	private Double y2;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	private LineString geom_way;
	
	public NetworkLink(){}

	public NetworkLink(Long id, Long osm_id, String osm_name, String osm_meta,
			Long osm_source_id, Long osm_target_id, Integer clazz,
			Integer flags, Integer source, Integer target, Double km,
			Integer kmh, Double cost, Double reverse_cost, Double x1,
			Double y1, Double x2, Double y2, LineString geom_way) {
		super();
		this.id = id;
		this.osm_id = osm_id;
		this.osm_name = osm_name;
		this.osm_meta = osm_meta;
		this.osm_source_id = osm_source_id;
		this.osm_target_id = osm_target_id;
		this.clazz = clazz;
		this.flags = flags;
		this.source = source;
		this.target = target;
		this.km = km;
		this.kmh = kmh;
		this.cost = cost;
		this.reverse_cost = reverse_cost;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.geom_way = geom_way;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(Long osm_id) {
		this.osm_id = osm_id;
	}

	public String getOsm_name() {
		return osm_name;
	}

	public void setOsm_name(String osm_name) {
		this.osm_name = osm_name;
	}

	public String getOsm_meta() {
		return osm_meta;
	}

	public void setOsm_meta(String osm_meta) {
		this.osm_meta = osm_meta;
	}

	public Long getOsm_source_id() {
		return osm_source_id;
	}

	public void setOsm_source_id(Long osm_source_id) {
		this.osm_source_id = osm_source_id;
	}

	public Long getOsm_target_id() {
		return osm_target_id;
	}

	public void setOsm_target_id(Long osm_target_id) {
		this.osm_target_id = osm_target_id;
	}

	public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public Integer getFlags() {
		return flags;
	}

	public void setFlags(Integer flags) {
		this.flags = flags;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public Double getKm() {
		return km;
	}

	public void setKm(Double km) {
		this.km = km;
	}

	public Integer getKmh() {
		return kmh;
	}

	public void setKmh(Integer kmh) {
		this.kmh = kmh;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getReverse_cost() {
		return reverse_cost;
	}

	public void setReverse_cost(Double reverse_cost) {
		this.reverse_cost = reverse_cost;
	}

	public Double getX1() {
		return x1;
	}

	public void setX1(Double x1) {
		this.x1 = x1;
	}

	public Double getY1() {
		return y1;
	}

	public void setY1(Double y1) {
		this.y1 = y1;
	}

	public Double getX2() {
		return x2;
	}

	public void setX2(Double x2) {
		this.x2 = x2;
	}

	public Double getY2() {
		return y2;
	}

	public void setY2(Double y2) {
		this.y2 = y2;
	}

	public LineString getGeom_way() {
		return geom_way;
	}

	public void setGeom_way(LineString geom_way) {
		this.geom_way = geom_way;
	}

	
	
}
