package es.udc.lbd.hermes.model.events;

public enum EventProcesor {

	VEHICLE_LOCATION, HIGH_SPEED, HIGH_ACCELERATION, HIGH_DECELERATION, HIGH_HEART_RATE, 
	DATA_SECTION, DRIVER_FEATURES, SLEEP_DATA, STEPS_DATA, HEART_RATE_DATA, CONTEXT_DATA,
	USER_ACTIVITIES, USER_LOCATIONS, FULL_USER_ACTIVITIES, FULL_USER_LOCATIONS, 
	USER_DISTANCES, USER_STEPS, USER_CALORIES_EXPENDED, USER_HEART_RATES, USER_SLEEP,
	FULL_USER_DISTANCES, FULL_USER_STEPS, FULL_USER_CALORIES_EXPENDED, FULL_USER_HEART_RATES;

	public String getName(){		
		return this.name();
	}

	public static EventProcesor getTipo(String tipo){
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
		case "Full User Activities":
			return FULL_USER_ACTIVITIES;
		case "Full User Locations":
			return FULL_USER_LOCATIONS;
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
		case "Full User Distances":
			return FULL_USER_DISTANCES;
		case "Full User Steps":
			return FULL_USER_STEPS;
		case "Full User Calories Expended":
			return FULL_USER_CALORIES_EXPENDED;
		case "Full User Heart Rates":
			return FULL_USER_HEART_RATES;
		
		default:
			return null;
		}
	}
}
