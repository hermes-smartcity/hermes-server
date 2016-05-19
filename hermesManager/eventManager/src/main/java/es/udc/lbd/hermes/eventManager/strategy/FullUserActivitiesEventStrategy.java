package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivity;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivityList;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.useractivities.service.UserActivitiesService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class FullUserActivitiesEventStrategy extends EventStrategy{

	@Override
	public void run() {

		start();
		UserActivitiesService userActivitiesService = ApplicationContextProvider.getApplicationContext().getBean("userActivitiesService", UserActivitiesService.class);
		
		ZtreamyUserActivityList ztreamyUserActivityList = (ZtreamyUserActivityList) event.getEventData();
		
		//Tenemos que borrar los eventos del dia completo. El día que hay que borrar es el del "startTime" 
		//del primer evento de la lista.
		if (ztreamyUserActivityList.getUserActivitiesList().size() > 0 ){
			ZtreamyUserActivity ztreamyUserActivity = ztreamyUserActivityList.getUserActivitiesList().get(0);
			Calendar startTime = ztreamyUserActivity.getStartTime();
			String sourceId = event.getSourceId();
			
			userActivitiesService.delete(sourceId, startTime);
		}
		
		//Insertamos todos los eventos del dia
		for (ZtreamyUserActivity ztreamyUserActivity : ztreamyUserActivityList.getUserActivitiesList()) {
			UserActivities userActivities = new UserActivities();			
			userActivities.setName(ztreamyUserActivity.getName());			
			userActivities.setStartTime(ztreamyUserActivity.getStartTime());
			userActivities.setEndTime(ztreamyUserActivity.getEndTime());
			userActivities.setEventId(event.getEventId());
			userActivitiesService.create(userActivities, event.getSourceId());
		}
		end();
	}
}
