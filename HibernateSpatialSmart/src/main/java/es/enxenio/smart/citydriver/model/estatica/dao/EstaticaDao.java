package es.enxenio.smart.citydriver.model.estatica.dao;

import java.util.List;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface EstaticaDao extends GenericDao<Estatica, Long> {	
	public List<Estatica> obterEstaticas();
}
