package es.udc.lbd.hermes.model.osmimport.dbconcept;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.dbconcept_id_seq")
@Table(schema="\"osmimport\"", name = "dbconcept")
@SuppressWarnings("serial")
public class DBConcept implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	private String schemaName;
	private String tableName;
	private String osmIdName;
	private String geomName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDbConnection")
	private DBConnection dbConnection;
	
	public DBConcept(){}

	public DBConcept(Long id, String name, String schemaName, String tableName,
			String osmIdName, String geomName, DBConnection dbConnection) {
		super();
		this.id = id;
		this.name = name;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.osmIdName = osmIdName;
		this.geomName = geomName;
		this.dbConnection = dbConnection;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getOsmIdName() {
		return osmIdName;
	}

	public void setOsmIdName(String osmIdName) {
		this.osmIdName = osmIdName;
	}

	public String getGeomName() {
		return geomName;
	}

	public void setGeomName(String geomName) {
		this.geomName = geomName;
	}

	public DBConnection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	
}
