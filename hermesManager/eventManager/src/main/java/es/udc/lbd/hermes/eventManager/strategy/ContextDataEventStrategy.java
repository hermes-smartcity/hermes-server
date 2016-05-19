package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;
import java.util.TimeZone;
import org.springframework.stereotype.Component;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextData;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextDataList;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.service.ContextDataService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class ContextDataEventStrategy extends EventStrategy {

	@Override
	public void run() {
		start();
		ContextDataService contextDataService = ApplicationContextProvider.getApplicationContext().getBean("contextDataService", ContextDataService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyContextDataList ztreamyContextData = (ZtreamyContextDataList) event.getEventData();
		Calendar dateTime = ztreamyContextData.getDateTime();
		for (ZtreamyContextData ztreamyContext : ztreamyContextData.getContextLogDetailList()) {
			ContextData contextData = new ContextData();
			contextData.setDetectedActivity(ztreamyContext.getDetectedActivity());
			contextData.setAccuracy(ztreamyContext.getAccuracy());
			Geometry punto = HelpersModel.prepararPunto(ztreamyContext.getLatitude(), ztreamyContext.getLongitude());
			contextData.setPosition((Point) punto);
			ztreamyContext.getTimeLog().set(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
			ztreamyContext.getTimeLog().setTimeZone(TimeZone.getDefault());
			contextData.setTimeLog(ztreamyContext.getTimeLog());
			contextData.setEventId(event.getEventId());
			contextDataService.create(contextData, event.getSourceId());
		}
		end();
	}
}