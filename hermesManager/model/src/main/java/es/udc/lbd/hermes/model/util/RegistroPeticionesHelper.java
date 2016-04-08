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
		dataService.setTimelog(HelpersModel.getHoy());

		
		dataServiceDao.create(dataService);
	}
	
	public void aggregateMeasurementSmartDriver(){
		DataServices dataService = new DataServices();
		dataService.setService(Service.SMARTDRIVER.toString());
		dataService.setMethod(Method.AGGREGATE_MEASUREMENT.toString());
		dataService.setTimelog(HelpersModel.getHoy());

		
		dataServiceDao.create(dataService);
	}
}
