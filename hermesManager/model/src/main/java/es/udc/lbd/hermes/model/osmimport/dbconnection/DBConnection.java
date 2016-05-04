package es.udc.lbd.hermes.model.osmimport.dbconnection;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "dbconnection_id_seq")
@Table(schema="\"osmimport\"", name = "dbconnection")
@SuppressWarnings("serial")
public class DBConnection implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	@Enumerated(EnumType.STRING)
    private DBConnectionType type;
	
	private String host;
	private Integer port;
	private String dbName;

	public DBConnection(){}

	public DBConnection(Long id, DBConnectionType type, String host,
			Integer port, String dbName) {
		super();
		this.id = id;
		this.type = type;
		this.host = host;
		this.port = port;
		this.dbName = dbName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DBConnectionType getType() {
		return type;
	}

	public void setType(DBConnectionType type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	
}
