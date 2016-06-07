package es.udc.lbd.hermes.model.events;

public enum EventType {

	VEHICLE_LOCATION, HIGH_SPEED, HIGH_ACCELERATION, HIGH_DECELERATION, HIGH_HEART_RATE, 
	DATA_SECTION, DRIVER_FEATURES, SLEEP_DATA, STEPS_DATA, HEART_RATE_DATA, CONTEXT_DATA,
	USER_ACTIVITIES, USER_LOCATIONS, USER_DISTANCES, USER_STEPS, USER_CALORIES_EXPENDED, 
	USER_HEART_RATES, USER_SLEEP;

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
		case "Sleep Data":
			return SLEEP_DATA;
		case "Steps Data":
			return STEPS_DATA;
		case "Context Data":
			return CONTEXT_DATA;
		case "Heart Rate Data":
			return HEART_RATE_DATA;
		case "User Activities":
			return USER_ACTIVITIES;
		case "User Locations":
			return USER_LOCATIONS;
		case "User Distances":
			return USER_DISTANCES;
		case "User Steps":
			return USER_STEPS;
		case "User Calories Expended":
			return USER_CALORIES_EXPENDED;
		case "User Heart Rates":
			return USER_HEART_RATES;
		case "User Sleep":
			return USER_SLEEP;
		default:
			return null;
		}
	}
}
