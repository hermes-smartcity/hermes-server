package es.udc.lbd.hermes.model.events;

public enum EventType {

	VEHICLE_LOCATION, HIGH_SPEED, HIGH_ACCELERATION, HIGH_DECELERATION, HIGH_HEART_RATE, DATA_SECTION, DRIVER_FEATURES;

	public String getName(){		
		return this.name();
	}

	public static EventType getTipo(String tipo){
		switch (tipo){
		case "Vehicle Location":
			return VEHICLE_LOCATION;
		case "High Speed":
			return HIGH_SPEED;
		case "High Acceleration":
			return HIGH_ACCELERATION;
		case "High Deceleration":
			return HIGH_DECELERATION;
		case "High Heart Rate":
			return HIGH_HEART_RATE;
		case "Data Section":
			return DATA_SECTION;
		case "Driver Features":
			return DRIVER_FEATURES;
		default:
			return null;
		}
	}
}