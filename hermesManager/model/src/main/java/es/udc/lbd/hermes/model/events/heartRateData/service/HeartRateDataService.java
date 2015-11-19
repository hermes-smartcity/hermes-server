package es.udc.lbd.hermes.model.events.heartRateData.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;

public interface HeartRateDataService {

	public HeartRateData get(Long id);
	
	public void create(HeartRateData sleepData, String sourceId);
	
	public void update(HeartRateData sleepData);
	
	public void delete(Long id);
	
	public List<HeartRateData> obterHeartRateData();
	
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario);
}
