package es.udc.lbd.signsrouter.builder;

import java.util.Set;

import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.TrafficSign;


public interface GraphBuilder {
	public Graph readGraph(Object... properties);
	
	public Set<TrafficSign> readTrafficSigns(Object... properties);
}
