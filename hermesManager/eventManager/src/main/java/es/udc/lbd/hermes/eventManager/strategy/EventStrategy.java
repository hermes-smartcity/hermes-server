package es.udc.lbd.hermes.eventManager.strategy;

import es.udc.lbd.hermes.eventManager.json.Event;

public abstract class EventStrategy {
	
	// Falta determinar parametros y el contenido del metodo en cada una de las clases
	// Procesa un evento, luego e implementara el metodo en cada tipo concreto de evento (vehicleLocation, highSpeed, highAcceleration, highDeceleration, highHeartRate, DataSection)
	 public abstract void processEvent(Event event);
}
