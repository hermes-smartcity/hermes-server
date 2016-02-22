package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyDataSection extends EventData {

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
	@JsonProperty("numHighAccelerations")
	private Double numHighAccelerations;
	@JsonProperty("numHighDecelerations")
	private Double numHighDecelerations;
	@JsonProperty("averageAcceleration")
	private Double averageAcceleration;
	@JsonProperty("averageDeceleration")
	private Double averageDeceleration;
	@JsonProperty("rrSection")
	private Double[] rrSection;
	@JsonProperty("roadSection")
	private List<RoadSectionPoint> roadSection;
	public Double getMinSpeed() {
		return minSpeed;
	}
	public void setMinSpeed(Double minSpeed) {
		this.minSpeed = minSpeed;
	}
	public Double getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(Double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public Double getMedianSpeed() {
		return medianSpeed;
	}
	public void setMedianSpeed(Double medianSpeed) {
		this.medianSpeed = medianSpeed;
	}
	public Double getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(Double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public Double getAverageRR() {
		return averageRR;
	}
	public void setAverageRR(Double averageRR) {
		this.averageRR = averageRR;
	}
	public Double getAverageHeartRate() {
		return averageHeartRate;
	}
	public void setAverageHeartRate(Double averageHeartRate) {
		this.averageHeartRate = averageHeartRate;
	}
	public Double getStandardDeviationSpeed() {
		return standardDeviationSpeed;
	}
	public void setStandardDeviationSpeed(Double standardDeviationSpeed) {
		this.standardDeviationSpeed = standardDeviationSpeed;
	}
	public Double getStandardDeviationRR() {
		return standardDeviationRR;
	}
	public void setStandardDeviationRR(Double standardDeviationRR) {
		this.standardDeviationRR = standardDeviationRR;
	}
	public Double getStandardDeviationHeartRate() {
		return standardDeviationHeartRate;
	}
	public void setStandardDeviationHeartRate(Double standardDeviationHeartRate) {
		this.standardDeviationHeartRate = standardDeviationHeartRate;
	}
	public Double getPke() {
		return pke;
	}
	public void setPke(Double pke) {
		this.pke = pke;
	}
	public List<RoadSectionPoint> getRoadSection() {
		return roadSection;
	}
	public void setRoadSection(List<RoadSectionPoint> roadSection) {
		this.roadSection = roadSection;
	}
	public Double getNumHighAccelerations() {
		return numHighAccelerations;
	}
	public void setNumHighAccelerations(Double numHighAccelerations) {
		this.numHighAccelerations = numHighAccelerations;
	}
	public Double getNumHighDecelerations() {
		return numHighDecelerations;
	}
	public void setNumHighDecelerations(Double numHighDecelerations) {
		this.numHighDecelerations = numHighDecelerations;
	}
	public Double getAverageAcceleration() {
		return averageAcceleration;
	}
	public void setAverageAcceleration(Double averageAcceleration) {
		this.averageAcceleration = averageAcceleration;
	}
	public Double getAverageDeceleration() {
		return averageDeceleration;
	}
	public void setAverageDeceleration(Double averageDeceleration) {
		this.averageDeceleration = averageDeceleration;
	}
	public Double[] getRrSection() {
		return rrSection;
	}
	public void setRrSection(Double[] rrSection) {
		this.rrSection = rrSection;
	}

}
