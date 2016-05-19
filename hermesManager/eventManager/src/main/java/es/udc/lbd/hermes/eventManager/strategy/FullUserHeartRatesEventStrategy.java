package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.ZtreamyUserHeartRates;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserHeartRatesList;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.service.UserHeartRatesService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class FullUserHeartRatesEventStrategy extends EventStrategy{

	@Override
	public void run() {

		start();
		UserHeartRatesService userHeartRatesService = ApplicationContextProvider.getApplicationContext().getBean("userHeartRatesService", UserHeartRatesService.class);
		
		ZtreamyUserHeartRatesList ztreamyUserHeartRatesList = (ZtreamyUserHeartRatesList) event.getEventData();
		
		//Tenemos que borrar los eventos del dia completo. El día que hay que borrar es el del "startTime" 
		//del primer evento de la lista.
		if (ztreamyUserHeartRatesList.getUserHeartRatesList().size() > 0 ){
			ZtreamyUserHeartRates ztreamyUserHeartRates = ztreamyUserHeartRatesList.getUserHeartRatesList().get(0);
			Calendar startTime = ztreamyUserHeartRates.getStartTime();
			String sourceId = event.getSourceId();
			
			userHeartRatesService.delete(sourceId, startTime);
		}
		
		//Insertamos todos los eventos del dia
		for (ZtreamyUserHeartRates ztreamyUserHeartRates : ztreamyUserHeartRatesList.getUserHeartRatesList()) {
			UserHeartRates userHeartRates = new UserHeartRates();			
			userHeartRates.setBpm(ztreamyUserHeartRates.getBpm());			
			userHeartRates.setStartTime(ztreamyUserHeartRates.getStartTime());
			userHeartRates.setEndTime(ztreamyUserHeartRates.getEndTime());
			userHeartRates.setEventId(event.getEventId());
			userHeartRatesService.create(userHeartRates, event.getSourceId());
		}
		end();
	}
}
