package es.udc.lbd.hermes.model.events.stepsData.dao;

import java.util.List;

import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface StepsDataDao extends GenericDao<StepsData, Long> {
	
	public List<StepsData> obterStepsData();
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario);
}
