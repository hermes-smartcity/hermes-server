package es.udc.lbd.hermes.model.osmimport.importshapefile;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;

public class ImportShapefile {

	private DBConnection dbConnection;
	private DBConcept dbConcept;
	private String dbConceptName;
	
	public ImportShapefile(){}

	public ImportShapefile(DBConnection dbConnection, DBConcept dbConcept,
			String dbConceptName) {
		super();
		this.dbConnection = dbConnection;
		this.dbConcept = dbConcept;
		this.dbConceptName = dbConceptName;
	}

	public DBConnection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public DBConcept getDbConcept() {
		return dbConcept;
	}

	public void setDbConcept(DBConcept dbConcept) {
		this.dbConcept = dbConcept;
	}

	public String getDbConceptName() {
		return dbConceptName;
	}

	public void setDbConceptName(String dbConceptName) {
		this.dbConceptName = dbConceptName;
	}

	
}
