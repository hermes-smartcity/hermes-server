package es.enxenio.smart.citydriver.model.estatica.service;

import java.util.List;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEstatica;


public interface EstaticaService {

	public Estatica get(Long id);
	
	public void create(Estatica estatica);
	
	public void crearEstatica(JSONEstatica estaticaJSON) throws InstanceNotFoundException;
	
	public void update(Estatica estatica);
	
	public void editarEstatica(JSONEstatica estaticaJSON) throws InstanceNotFoundException;
	
	public void delete(Long id);

	public List<Estatica> obterEstaticas();
	
	public void actualizarTitulosDeEstaticas(List<JSONEstatica> estaticas);
}
