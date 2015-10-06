package es.udc.lbd.signsrouter.navigator;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.TrafficSign;

public class DetectedSign implements Comparable<DetectedSign> {
	public TrafficSign sign;
	public Edge edge;
	public float score;
	
	public DetectedSign(TrafficSign sign, Edge edge, float score) {
		super();
		this.sign = sign;
		this.edge = edge;
		this.score = score;
	}

	public int compareTo(DetectedSign o) {
		return (int) (this.score - o.score);
	}
}
