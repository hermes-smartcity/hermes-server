package es.udc.lbd.hermes.model.smartdriver.service;

import java.util.List;

import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.Type;

public interface NetworkService {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
	public AggregateMeasurementVO getAggregateMeasurement(Type type, Double lat, Double lon, Integer day, Integer time, String value);
	public List<RouteSegment> getComputeRoute(Double fromLat, Double fromLng, Double toLat, Double toLng);
}
