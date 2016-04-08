package es.udc.lbd.hermes.model.smartdriver.dao;

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface NetworkDao extends GenericDao<NetworkLink, Long> {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
}
