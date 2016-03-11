package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyContextDataList extends EventData {

	@JsonProperty("dateTime")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Calendar dateTime;
	@JsonProperty("contextLogDetailList")
	private List<ZtreamyContextData> contextLogDetailList;
	
	public Calendar getDateTime() {
		return dateTime;
	}
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}
	public List<ZtreamyContextData> getContextLogDetailList() {
		return contextLogDetailList;
	}
	public void setContextLogDetailList(List<ZtreamyContextData> contextLogDetailList) {
		this.contextLogDetailList = contextLogDetailList;
	}
	
}
