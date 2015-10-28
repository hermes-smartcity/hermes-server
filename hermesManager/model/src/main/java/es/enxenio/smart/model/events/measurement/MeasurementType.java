package es.enxenio.smart.model.events.measurement;

public enum MeasurementType {

	HIGH_SPEED, HIGH_ACCELERATION, HIGH_DECELERATION, HIGH_HEART_RATE;

	public String getName(){		
		return this.name();
	}

	public static MeasurementType getTipo(String tipo){
		switch (tipo){
		case "HighSpeed":
			return HIGH_SPEED;
		case "HighAcceleration":
			return HIGH_ACCELERATION;
		case "HighDeceleration":
			return HIGH_DECELERATION;
		case "HighHeartRate":
			return HIGH_HEART_RATE;
		default:
			return null;
		}
	}
}

