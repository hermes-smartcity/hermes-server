package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.ZtreamyUserSteps;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserStepsList;
import es.udc.lbd.hermes.model.events.usersteps.UserSteps;
import es.udc.lbd.hermes.model.events.usersteps.service.UserStepsService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class UserStepsEventStrategy extends EventStrategy{

	@Override
	public void run() {

		start();
		UserStepsService userStepsService = ApplicationContextProvider.getApplicationContext().getBean("userStepsService", UserStepsService.class);
		
		ZtreamyUserStepsList ztreamyUserStepsList = (ZtreamyUserStepsList) event.getEventData();
		for (ZtreamyUserSteps ztreamyUserSteps : ztreamyUserStepsList.getUserStepsList()) {
			UserSteps userSteps = new UserSteps();			
			userSteps.setSteps(ztreamyUserSteps.getSteps());			
			userSteps.setStartTime(ztreamyUserSteps.getStartTime());
			userSteps.setEndTime(ztreamyUserSteps.getEndTime());
			userSteps.setEventId(event.getEventId());
			userStepsService.create(userSteps, event.getSourceId());
		}
		end();
	}
}
