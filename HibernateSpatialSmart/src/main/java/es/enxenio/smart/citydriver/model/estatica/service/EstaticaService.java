package es.enxenio.smart.citydriver.model.estatica.service;

import java.util.List;

import es.enxenio.smart.citydriver.model.estatica.Estatica;


public interface EstaticaService {

	public Estatica get(Long id);
	
	public void create(Estatica dataSection);
	
	public void update(Estatica dataSection);
	
	public void delete(Long id);

	public List<Estatica> obterEstaticas();
}
