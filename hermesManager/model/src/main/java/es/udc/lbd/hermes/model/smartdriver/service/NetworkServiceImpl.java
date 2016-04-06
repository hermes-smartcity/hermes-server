package es.udc.lbd.hermes.model.smartdriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.smartdriver.NetworkLine;
import es.udc.lbd.hermes.model.smartdriver.dao.NetworkDao;

@Service("networkService")
@Transactional
public class NetworkServiceImpl implements NetworkService{

	@Autowired
	private NetworkDao networkDao;
	
	@Override
	@Transactional(readOnly = true)
	public NetworkLine getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat){
		NetworkLine network = networkDao.getLinkInformation(currentLong, currentLat, previousLong, previousLat);
		
		return network;
	}
}
