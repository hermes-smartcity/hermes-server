package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


public abstract class EventStrategy {
	
	// Falta determinar parametros y el contenido del metodo en cada una de las clases
	// Procesa un evento, luego e implementara el metodo en cada tipo concreto de evento (vehicleLocation, highSpeed, highAcceleration, highDeceleration, highHeartRate, DataSection)
	 public abstract void processEvent(JSONObject evento);
}
