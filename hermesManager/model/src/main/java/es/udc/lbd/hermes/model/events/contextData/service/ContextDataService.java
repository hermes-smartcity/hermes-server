package es.udc.lbd.hermes.model.events.contextData.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.contextData.ContextData;

public interface ContextDataService {

	public ContextData get(Long id);
	
	public void create(ContextData contextData, String sourceId);
	
	public void update(ContextData contextData);
	
	public void delete(Long id);
	
	public List<ContextData> obterContextData();
	
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario);
}
