package es.udc.lbd.signsrouter.navigator;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;

public interface Navigator {
	public void navigate(Graph graph, Edge origin);
}
