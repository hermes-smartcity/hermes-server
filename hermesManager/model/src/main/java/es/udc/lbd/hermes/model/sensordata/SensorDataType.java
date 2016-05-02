package es.udc.lbd.hermes.model.sensordata;

public enum SensorDataType {

	TYPE_ACCELEROMETER, TYPE_AMBIENT_TEMPERATURE, TYPE_GRAVITY, TYPE_GYROSCOPE,
	TYPE_LIGHT, TYPE_LINEAR_ACCELERATION, TYPE_MAGNETIC_FIELD, TYPE_ORIENTATION, TYPE_PRESSURE,
	TYPE_PROXIMITY, TYPE_RELATIVE_HUMIDITY, TYPE_ROTATION_VECTOR, TYPE_TEMPERATURE;

	public String getName(){		
		return this.name();
	}

}
