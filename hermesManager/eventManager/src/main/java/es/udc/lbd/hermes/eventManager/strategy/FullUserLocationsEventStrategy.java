package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.ZtreamyUserLocation;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserLocationList;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.userlocations.service.UserLocationsService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class FullUserLocationsEventStrategy extends EventStrategy{

	@Override
	public void run() {

		start();
		UserLocationsService userLocationsService = ApplicationContextProvider.getApplicationContext().getBean("userLocationsService", UserLocationsService.class);
		
		ZtreamyUserLocationList ztreamyUserLocationList = (ZtreamyUserLocationList) event.getEventData();
		
		//Tenemos que borrar los eventos del dia completo. El día que hay que borrar es el del "startTime" 
		//del primer evento de la lista.
		if (ztreamyUserLocationList.getUserLocationsList().size() > 0 ){
			ZtreamyUserLocation ztreamyUserLocation = ztreamyUserLocationList.getUserLocationsList().get(0);
			Calendar startTime = ztreamyUserLocation.getStartTime();
			String sourceId = event.getSourceId();

			userLocationsService.delete(sourceId, startTime);
		}

		//Insertamos todos los eventos del dia			
		for (ZtreamyUserLocation ztreamyUserLocation : ztreamyUserLocationList.getUserLocationsList()) {
			UserLocations userLocations = new UserLocations();
			userLocations.setAccuracy(ztreamyUserLocation.getAccuracy());
			Geometry punto = HelpersModel.prepararPunto(ztreamyUserLocation.getLatitude(), ztreamyUserLocation.getLongitude());
			userLocations.setPosition((Point)punto);
			userLocations.setStartTime(ztreamyUserLocation.getStartTime());
			userLocations.setEndTime(ztreamyUserLocation.getEndTime());
			userLocations.setEventId(event.getEventId());
			userLocationsService.create(userLocations, event.getSourceId());
		}
		end();
	}
}
