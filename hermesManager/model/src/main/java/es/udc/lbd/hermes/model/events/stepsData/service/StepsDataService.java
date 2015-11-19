package es.udc.lbd.hermes.model.events.stepsData.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.stepsData.StepsData;

public interface StepsDataService {

	public StepsData get(Long id);
	
	public void create(StepsData sleepData, String sourceId);
	
	public void update(StepsData sleepData);
	
	public void delete(Long id);
	
	public List<StepsData> obterStepsData();
	
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario);
}
