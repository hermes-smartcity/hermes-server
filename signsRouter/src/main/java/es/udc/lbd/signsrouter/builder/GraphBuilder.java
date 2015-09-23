package es.udc.lbd.signsrouter.builder;

import es.udc.lbd.signsrouter.model.Graph;


public interface GraphBuilder {
	public Graph readGraph(Object... properties);
}
