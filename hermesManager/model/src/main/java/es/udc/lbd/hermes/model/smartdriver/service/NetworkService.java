package es.udc.lbd.hermes.model.smartdriver.service;

import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;

public interface NetworkService {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
}
