package es.udc.lbd.hermes.model.sensordata;

import java.util.ArrayList;
import java.util.List;


public class Row {
	// Lista de cada uno de los puntos de la gráfica 
	private List<C> rows = new ArrayList<C>();
	
	public Row(){}

	public Row(List<C> rows) {		
		super();
		this.rows = rows;
	}

	public List<C> getRows() {
		return rows;
	}

	public void setRows(List<C> rows) {
		this.rows = rows;
	}
	
	public void addC(C c) {
		this.rows.add(c);
	}
	
}
