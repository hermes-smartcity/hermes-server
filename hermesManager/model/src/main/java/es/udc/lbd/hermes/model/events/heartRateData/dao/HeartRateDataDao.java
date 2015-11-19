package es.udc.lbd.hermes.model.events.heartRateData.dao;

import java.util.List;

import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface HeartRateDataDao extends GenericDao<HeartRateData, Long> {
	
	public List<HeartRateData> obterHeartRateData();
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario);
}
