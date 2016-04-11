package es.udc.lbd.hermes.model.smartdriver.service;

import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.Type;

public interface NetworkService {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
	public AggregateMeasurementVO getAggregateMeasurement(Type type, Double lat, Double lon, Integer day, Integer time, String value);
	
}
