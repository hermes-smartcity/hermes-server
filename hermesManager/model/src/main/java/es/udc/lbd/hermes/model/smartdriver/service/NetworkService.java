package es.udc.lbd.hermes.model.smartdriver.service;

import es.udc.lbd.hermes.model.smartdriver.NetworkLine;

public interface NetworkService {

	public NetworkLine getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
}
