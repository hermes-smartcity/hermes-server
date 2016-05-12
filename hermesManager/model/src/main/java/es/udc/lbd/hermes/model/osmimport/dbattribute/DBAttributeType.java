package es.udc.lbd.hermes.model.osmimport.dbattribute;

public enum DBAttributeType {

	DATE, NUMBER_LONG, NUMBER_DOUBLE, NUMBER_INT, NUMBER_FLOAT, CHAR, BOOLEAN;

	public String getName(){		
		return this.name();
	}
}
