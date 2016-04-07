package es.udc.lbd.hermes.model.smartdriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.dataservice.dao.DataServicesDao;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.dao.NetworkDao;
import es.udc.lbd.hermes.model.util.RegistroPeticionesHelper;


@Service("networkService")
@Transactional
public class NetworkServiceImpl implements NetworkService{

	@Autowired
	private NetworkDao networkDao;
	
	@Autowired
	private DataServicesDao dataServiceDao;
	
	@Override
	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat){
		//Recuperamos el datos
		NetworkLinkVO network = networkDao.getLinkInformation(currentLong, currentLat, previousLong, previousLat);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.linkInformationSmartDriver();
		
		return network;
	}
}
