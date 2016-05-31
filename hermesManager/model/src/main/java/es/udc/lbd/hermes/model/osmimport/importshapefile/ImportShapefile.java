package es.udc.lbd.hermes.model.osmimport.importshapefile;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;

public class ImportShapefile {

	private DBConnection dbConnection;
	private DBConcept dbConcept;
	private String dbConceptSchema;
	private String dbConceptName;
	private Boolean keepExistingData;
	
	public ImportShapefile(){}

	public ImportShapefile(DBConnection dbConnection, DBConcept dbConcept,
			String dbConceptSchema, String dbConceptName, Boolean keepExistingData) {
		super();
		this.dbConnection = dbConnection;
		this.dbConcept = dbConcept;
		this.dbConceptSchema = dbConceptSchema;
		this.dbConceptName = dbConceptName;
		this.keepExistingData = keepExistingData;
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

	public String getDbConceptSchema() {
		return dbConceptSchema;
	}

	public void setDbConceptSchema(String dbConceptSchema) {
		this.dbConceptSchema = dbConceptSchema;
	}

	public String getDbConceptName() {
		return dbConceptName;
	}

	public void setDbConceptName(String dbConceptName) {
		this.dbConceptName = dbConceptName;
	}

	public Boolean getKeepExistingData() {
		return keepExistingData;
	}

	public void setKeepExistingData(Boolean keepExistingData) {
		this.keepExistingData = keepExistingData;
	}

}
