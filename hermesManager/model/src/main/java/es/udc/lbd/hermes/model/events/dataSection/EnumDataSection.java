package es.udc.lbd.hermes.model.events.dataSection;

public enum EnumDataSection {

	minSpeed, maxSpeed, medianSpeed, averageSpeed, averageRR, averageHeartRate, standardDeviationSpeed,
	standardDeviationRR, standardDeviationHeartRate, pke, numHighAccelerations, numHighDecelerations, 
	averageAcceleration, averageDeceleration;

	public String getName(){		
		return this.name();
	}

}
