package es.udc.lbd.hermes.model.util;

import es.udc.lbd.hermes.model.dataservice.DataServices;
import es.udc.lbd.hermes.model.dataservice.Method;
import es.udc.lbd.hermes.model.dataservice.Service;
import es.udc.lbd.hermes.model.dataservice.dao.DataServicesDao;

public class RegistroPeticionesHelper {

	private DataServicesDao dataServiceDao;

	public RegistroPeticionesHelper(DataServicesDao dataServiceDao){
		this.dataServiceDao = dataServiceDao;
	}

	public void linkInformationSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.GET_INFORMATION_LINK.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void aggregateMeasurementSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.AGGREGATE_MEASUREMENT.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void computeRouteSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.COMPUTE_ROUTE.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}

	public void simulateRouteSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.SIMULATE_ROUTE.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void vehicleLocationSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.GET_VEHICLE_LOCATIONS.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void measurementSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.GET_MEASUREMENTS.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void dataSectionsSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.GET_DATA_SECTIONS.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void driverFeaturesSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.GET_DRIVER_FEATURES.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
		
	public void heartRateDataSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_HEART_RATE_DATA.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void stepsDataSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_STEPS_DATA.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void sleepDataSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_SLEEP_DATA.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void contextDataSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_CONTEXT_DATA.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void userLocationsSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_USER_LOCATIONS.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
	
	public void userActivitiesSmartCitizien(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTCITIZEN.toString());
		dataService.setMethod(Method.GET_USER_ACTIVITIES.toString());
		
		String formato = "yyyy-MM-dd HH:mm:ss";
		String fechaHoy = HelpersModel.obtenerHoySegunFormato(formato);
		dataService.setTimelog(HelpersModel.getFecha(fechaHoy, formato));
		
		dataServiceDao.create(dataService);
	}
}
