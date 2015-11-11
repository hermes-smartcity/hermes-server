package es.udc.lbd.hermes.model.events.measurement;

public enum MeasurementType {

	HIGH_SPEED, HIGH_ACCELERATION, HIGH_DECELERATION, HIGH_HEART_RATE;

	public String getName(){		
		return this.name();
	}

	public static MeasurementType getTipo(String tipo){
		switch (tipo){
		case "High Speed":
			return HIGH_SPEED;
		case "High Acceleration":
			return HIGH_ACCELERATION;
		case "High Deceleration":
			return HIGH_DECELERATION;
		case "High Heart Rate":
			return HIGH_HEART_RATE;
		default:
			return null;
		}
	}
}

