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
}
