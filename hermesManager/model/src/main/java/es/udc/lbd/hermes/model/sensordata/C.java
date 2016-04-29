package es.udc.lbd.hermes.model.sensordata;

import java.util.ArrayList;
import java.util.List;


public class C {
	// Lista de cada uno de los puntos de la gráfica 
	private List<V> c = new ArrayList<V>();
	
	public C(){}

	public C(List<V> c) {		
		super();
		this.c = c;
	}

	public List<V> getC() {
		return c;
	}

	public void setC(List<V> c) {
		this.c = c;
	}
	
	public void addV(V v) {
		this.c.add(v);
	}
}
