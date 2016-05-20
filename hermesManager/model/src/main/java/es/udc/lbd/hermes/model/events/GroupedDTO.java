package es.udc.lbd.hermes.model.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;

public class GroupedDTO {

	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomPointDeserializer.class)	
    private Point geom;
	private int count;
	
	public GroupedDTO(){}

	public GroupedDTO(Point geom, int count) {
		super();
		this.geom = geom;
		this.count = count;
	}

	public Point getGeom() {
		return geom;
	}

	public void setGeom(Point geom) {
		this.geom = geom;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
