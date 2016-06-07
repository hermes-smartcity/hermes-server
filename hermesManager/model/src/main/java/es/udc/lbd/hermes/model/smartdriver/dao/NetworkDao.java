package es.udc.lbd.hermes.model.smartdriver.dao;

import java.util.List;

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.OriginDestinyPoint;
import es.udc.lbd.hermes.model.smartdriver.RoutePoint;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.util.dao.GenericDao;
import es.udc.lbd.hermes.model.util.exceptions.RouteException;

public interface NetworkDao extends GenericDao<NetworkLink, Long> {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
	
	public OriginDestinyPoint obtainOriginPoint(Double fromLat, Double fromLng);
	public OriginDestinyPoint obtainDestinyPoint(Double toLat, Double toLng);
	public List<RouteSegment> obtainListSections(OriginDestinyPoint originPoint, OriginDestinyPoint destinyPoint, Double fromLat, Double fromLng)  throws RouteException;
	public List<RoutePoint> simulateListSections(OriginDestinyPoint originPoint, OriginDestinyPoint destinyPoint, Double fromLat, Double fromLng, Double sf, Double secondsPerStep)  throws RouteException;
}
