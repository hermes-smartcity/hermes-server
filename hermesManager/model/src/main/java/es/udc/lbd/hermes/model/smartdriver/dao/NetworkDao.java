package es.udc.lbd.hermes.model.smartdriver.dao;

import java.util.List;

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.util.dao.GenericDao;
import es.udc.lbd.hermes.model.util.exceptions.RouteException;

public interface NetworkDao extends GenericDao<NetworkLink, Long> {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
	
	public Integer obtainOriginPoint(Double fromLat, Double fromLng);
	public Integer obtainDestinyPoint(Double toLat, Double toLng);
	public List<RouteSegment> obtainListSections(Integer originPoint, Integer destinyPoint)  throws RouteException;
}
