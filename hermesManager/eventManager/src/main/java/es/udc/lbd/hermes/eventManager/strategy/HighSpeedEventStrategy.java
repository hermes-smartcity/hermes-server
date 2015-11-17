package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighSpeed;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class HighSpeedEventStrategy extends EventStrategy {
		
	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyHighSpeed ztreamyHighSpeed = (ZtreamyHighSpeed) event.getEventData();
		Measurement measurement = new Measurement();
		Geometry punto = HelpersModel.prepararPunto(ztreamyHighSpeed.getLatitude(),ztreamyHighSpeed.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighSpeed.getValue());
		measurement.setTipo(MeasurementType.HIGH_SPEED);
		measurement.setEventId(event.getEventId());

		measurement.setTimestamp(event.getTimestamp());
		measurementService.create(measurement, event.getSourceId());
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}