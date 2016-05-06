package es.udc.lbd.hermes.model.osmimport.osmfilter;

public enum OsmFilterOperation {

	EQUALS, DIFFERENT, LIKE, NOTLIKE;

	public String getName(){		
		return this.name();
	}
}
