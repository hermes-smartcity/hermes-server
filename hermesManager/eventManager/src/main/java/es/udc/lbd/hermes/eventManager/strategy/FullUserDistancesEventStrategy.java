package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.ZtreamyUserDistances;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserDistancesList;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;
import es.udc.lbd.hermes.model.events.userdistances.service.UserDistancesService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class FullUserDistancesEventStrategy extends EventStrategy{

	@Override
	public void run() {

		start();
		UserDistancesService userDistancesService = ApplicationContextProvider.getApplicationContext().getBean("userDistancesService", UserDistancesService.class);
		
		ZtreamyUserDistancesList ztreamyUserDistancesList = (ZtreamyUserDistancesList) event.getEventData();
		
		//Tenemos que borrar los eventos del dia completo. El día que hay que borrar es el del "startTime" 
		//del primer evento de la lista.
		if (ztreamyUserDistancesList.getUserDistancesList().size() > 0 ){
			ZtreamyUserDistances ztreamyUserDistances = ztreamyUserDistancesList.getUserDistancesList().get(0);
			Calendar startTime = ztreamyUserDistances.getStartTime();
			String sourceId = event.getSourceId();
			
			userDistancesService.delete(sourceId, startTime);
		}
		
		//Insertamos todos los eventos del dia
		for (ZtreamyUserDistances ztreamyUserDistances : ztreamyUserDistancesList.getUserDistancesList()) {
			UserDistances userDistances = new UserDistances();			
			userDistances.setDistance(ztreamyUserDistances.getDistance());			
			userDistances.setStartTime(ztreamyUserDistances.getStartTime());
			userDistances.setEndTime(ztreamyUserDistances.getEndTime());
			userDistances.setEventId(event.getEventId());
			userDistancesService.create(userDistances, event.getSourceId());
		}
		end();
	}
}
