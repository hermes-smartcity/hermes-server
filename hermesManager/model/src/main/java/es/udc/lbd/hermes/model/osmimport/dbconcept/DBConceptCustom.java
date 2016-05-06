package es.udc.lbd.hermes.model.osmimport.dbconcept;

public class DBConceptCustom {

	private Long id;
	private String schemaName;
	private String tableName;
	private String dbConnection;
	
	public DBConceptCustom(){}

	public DBConceptCustom(Long id, String schemaName, String tableName,
			String dbConnection) {
		super();
		this.id = id;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.dbConnection = dbConnection;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(String dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	
}
