package es.udc.lbd.hermes.eventManager.controller.util;


public class JSONDataType {

	private String value;
	
	private int valueInt;

	private Long valueL;
	
	private String type;
	private String key;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getValueInt() {
		return valueInt;
	}

	public void setValueInt(int valueInt) {
		this.valueInt = valueInt;
	}

	public Long getValueL() {
		return valueL;
	}

	public void setValueL(Long valueL) {
		this.valueL = valueL;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
