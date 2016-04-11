package es.udc.lbd.hermes.eventManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.controller.events.dashboard.Statistics;
import es.udc.lbd.hermes.model.events.contextData.service.ContextDataService;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.events.heartRateData.service.HeartRateDataService;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.sleepData.service.SleepDataService;
import es.udc.lbd.hermes.model.events.stepsData.service.StepsDataService;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component("statisticsService")
public class StatisticsService {

	@Autowired private UsuarioMovilService usuarioMovilService;

	@Autowired private UsuarioWebService usuarioWebService;

	@Autowired private VehicleLocationService vehicleLocationService;

	@Autowired private DataSectionService dataSectionService;

	@Autowired private MeasurementService measurementService;

	@Autowired private DriverFeaturesService driverFeaturesService;

	@Autowired private StepsDataService stepsDataService;

	@Autowired private SleepDataService sleepDataService;

	@Autowired private HeartRateDataService heartRateDataService;

	@Autowired private ContextDataService contextDataService;
	
	@Scheduled(fixedRate = 60000) //ejecutado cada minuto
	public void updateStatistics(){
		//Cada 60 segundos, recuperamos el objeto statistics y actualizamos sus valores
		Statistics statistics = ApplicationContextProvider.getApplicationContext().getBean("statisticsComponent", Statistics.class);
		
		statistics.updateStatistics();
	
	}
}
