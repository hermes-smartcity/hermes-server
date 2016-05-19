package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.ZtreamyHighAcceleration;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class HighAccelerationEventStrategy extends EventStrategy {
		
	@Override
	public void run() {
		
		start();
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyHighAcceleration ztreamyHighAcceleration = (ZtreamyHighAcceleration) event.getEventData();
		Measurement measurement = new Measurement();
		Geometry punto = HelpersModel.prepararPunto(ztreamyHighAcceleration.getLatitude(),ztreamyHighAcceleration.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setAccuracy(ztreamyHighAcceleration.getAccuracy());
		measurement.setSpeed(ztreamyHighAcceleration.getSpeed());
		measurement.setValue(ztreamyHighAcceleration.getValue());
		measurement.setTipo(MeasurementType.HIGH_ACCELERATION);
		measurement.setEventId(event.getEventId());

		measurement.setTimestamp(event.getTimestamp());
		measurementService.create(measurement, event.getSourceId());
		end();
	}

}