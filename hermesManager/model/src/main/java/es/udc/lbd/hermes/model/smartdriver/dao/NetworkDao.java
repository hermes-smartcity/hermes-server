package es.udc.lbd.hermes.model.smartdriver.dao;

import es.udc.lbd.hermes.model.smartdriver.Network;
import es.udc.lbd.hermes.model.smartdriver.NetworkLine;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface NetworkDao extends GenericDao<Network, Long> {

	public NetworkLine getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
}
