package es.udc.lbd.hermes.eventManager.strategy;

import java.util.List;
import org.springframework.stereotype.Component;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import es.udc.lbd.hermes.eventManager.json.RoadSectionPoint;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class DataSectionEventStrategy extends EventStrategy {
	
	@Override
	public void run() {

		start();
		VehicleLocationService vehicleLocationService = ApplicationContextProvider.getApplicationContext().getBean("vehicleLocationService", VehicleLocationService.class);		
		// Construir un objeto del modelo a partir del evento
		ZtreamyDataSection ztreamyDataSection = (ZtreamyDataSection) event.getEventData();
		List<RoadSectionPoint> vehicles = ztreamyDataSection.getRoadSection();
		Double[] rrSection = ztreamyDataSection.getRrSection();
		
		for (int i=0;i<vehicles.size();i++){
			RoadSectionPoint vehicle = vehicles.get(i);
			
			Double rr = null;
			if (rrSection!=null && rrSection.length > 0){
				rr = rrSection[i];
			}
			
			VehicleLocation vehicleLocation = new VehicleLocation();	
			Geometry punto = HelpersModel.prepararPunto(vehicle.getLatitude(), vehicle.getLongitude());
			vehicleLocation.setPosition((Point)punto);
			vehicleLocation.setAccuracy(vehicle.getAccuracy());
			vehicleLocation.setSpeed(vehicle.getSpeed());
			vehicleLocation.setRr(rr);
			vehicleLocation.setEventId(event.getEventId());
			
			vehicleLocation.setTimestamp(event.getTimestamp());
			vehicleLocationService.create(vehicleLocation, event.getSourceId());
		}
		end();
	}
}