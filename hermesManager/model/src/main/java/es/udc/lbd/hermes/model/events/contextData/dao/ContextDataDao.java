package es.udc.lbd.hermes.model.events.contextData.dao;

import java.util.List;

import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface ContextDataDao extends GenericDao<ContextData, Long> {
	
	public List<ContextData> obterContextData();
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario);
}
