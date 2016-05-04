package es.udc.lbd.hermes.model.osmimport.dbconnection;

public enum DBConnectionType {

	POSTGRESQL, MYSQL;

	public String getName(){		
		return this.name();
	}
}
