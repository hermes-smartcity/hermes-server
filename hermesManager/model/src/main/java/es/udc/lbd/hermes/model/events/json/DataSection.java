package es.udc.lbd.hermes.model.events.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSection extends EventData {

	@JsonProperty("minSpeed")
	private Double minSpeed;
	@JsonProperty("maxSpeed")
	private Double maxSpeed;
	@JsonProperty("medianSpeed")
	private Double medianSpeed;
	@JsonProperty("averageSpeed")
	private Double averageSpeed;
	@JsonProperty("averageRR")
	private Double averageRR;
	@JsonProperty("averageHeartRate")
	private Double averageHeartRate;
	@JsonProperty("standardDeviationSpeed")
	private Double standardDeviationSpeed;
	@JsonProperty("standardDeviationRR")
	private Double standardDeviationRR;
	@JsonProperty("standardDeviationHeartRate")
	private Double standardDeviationHeartRate;
	@JsonProperty("pke")
	private Double pke;
	@JsonProperty("roadSection")
	private List<RoadSectionPoint> roadSection;
}
