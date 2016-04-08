package es.udc.lbd.hermes.model.smartdriver;

import java.io.Serializable;
import java.math.BigInteger;

public class AggregateMeasurementVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigInteger numberOfValues;
	private Double max;
	private Double min;
	private Double average;
	private Double standardDeviation;
	
	public AggregateMeasurementVO(){}

	public AggregateMeasurementVO(BigInteger numberOfValues, Double max,
			Double min, Double average, Double standardDeviation) {
		super();
		this.numberOfValues = numberOfValues;
		this.max = max;
		this.min = min;
		this.average = average;
		this.standardDeviation = standardDeviation;
	}

	public BigInteger getNumberOfValues() {
		return numberOfValues;
	}

	public void setNumberOfValues(BigInteger numberOfValues) {
		this.numberOfValues = numberOfValues;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	

}
