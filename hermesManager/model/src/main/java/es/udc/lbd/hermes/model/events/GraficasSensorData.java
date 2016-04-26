package es.udc.lbd.hermes.model.events;

import java.math.BigDecimal;
import java.util.List;

public class GraficasSensorData {

	private List<String> labels;
	private List<String> series;
	private List<List<BigDecimal>> data;
	
	public GraficasSensorData(){}

	public GraficasSensorData(List<String> labels, List<String> series,
			List<List<BigDecimal>> data) {
		super();
		this.labels = labels;
		this.series = series;
		this.data = data;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

	public List<List<BigDecimal>> getData() {
		return data;
	}

	public void setData(List<List<BigDecimal>> data) {
		this.data = data;
	}
	
	
	
}
