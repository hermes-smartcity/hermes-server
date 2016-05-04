package es.udc.lbd.hermes.model.osmimport.dbattribute;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "dbattribute_id_seq")
@Table(schema="\"osmimport\"", name = "dbattribute")
@SuppressWarnings("serial")
public class DBAttribute implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String attributeName;
	
	@Enumerated(EnumType.STRING)
	private DBAttributeType attributeType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDbConcept")
	private DBConcept dbConcept;
	
	public DBAttribute(){}

	public DBAttribute(Long id, String attributeName,
			DBAttributeType attributeType, DBConcept dbConcept) {
		super();
		this.id = id;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.dbConcept = dbConcept;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public DBAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(DBAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public DBConcept getDbConcept() {
		return dbConcept;
	}

	public void setDbConcept(DBConcept dbConcept) {
		this.dbConcept = dbConcept;
	}
	
	
}
