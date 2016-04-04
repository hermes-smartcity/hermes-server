package es.udc.lbd.hermes.model.setting;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "setting_id_seq")
@Table(name = "setting")
@SuppressWarnings("serial")
public class Setting implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	private String valueChar;
	private Double valueNumber;
	private String type;
	
	public Setting(){}

	public Setting(Long id, String name, String valueChar, Double valueNumber, String type) {
		super();
		this.id = id;
		this.name = name;
		this.valueChar = valueChar;
		this.valueNumber = valueNumber;
		this.type = type;
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

	public String getValueChar() {
		return valueChar;
	}

	public void setValueChar(String valueChar) {
		this.valueChar = valueChar;
	}

	public Double getValueNumber() {
		return valueNumber;
	}

	public void setValueNumber(Double valueNumber) {
		this.valueNumber = valueNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
