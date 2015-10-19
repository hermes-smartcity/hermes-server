package es.enxenio.smart.citydriver.model.estatica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.estatica.dao.EstaticaDao;
import es.enxenio.smart.citydriver.model.usuario.dao.UsuarioDao;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEstatica;

@Service("estaticaService")
@Transactional
public class EstaticaServiceImpl implements EstaticaService {
	
	@Autowired
	private EstaticaDao estaticaDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public Estatica get(Long id) {
		return estaticaDao.get(id);
	}

	@Override
	public void create(Estatica estatica) {			
		estaticaDao.create(estatica);		
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { InstanceNotFoundException.class })
	public void crearEstatica(JSONEstatica estaticaJSON)
			throws InstanceNotFoundException {
		Estatica estatica = new Estatica();
		estatica.setTitulo(estaticaJSON.getTitulo());
		estatica.setContenido(estaticaJSON.getContenido());
		
		estaticaDao.save(estatica);
	}
	
	public void editarEstatica(JSONEstatica estaticaJSON) throws InstanceNotFoundException{		
		Estatica estatica = estaticaDao.get(estaticaJSON.getId());
		// Por si modifican con el inspeccionar o algo el id y me mandan algo erróneo...
		if(estatica!=null){
			estatica.setTitulo(estaticaJSON.getTitulo());
			estatica.setContenido(estaticaJSON.getContenido());
		}
	}
	
	@Override
	public void update(Estatica estatica) {
		estaticaDao.update(estatica);
	}

	@Override
	public void delete(Long id) {
		// Se controla en angular, pero por si nos mandan algo con inspeccionar y empezamos a borrar cosas que no se deben o nos mandan null y casca
		if(id!=null){
			Estatica estatica = estaticaDao.get(id);
			if (estatica != null) {
				estaticaDao.delete(id);
			}					
		}
	}
	
	@Transactional(readOnly = true)
	public List<Estatica> obterEstaticas() {
		List<Estatica> estaticas = estaticaDao.obterEstaticas();
		return estaticas;
	}
	
	public void actualizarTitulosDeEstaticas(List<JSONEstatica> estaticas){
		for(JSONEstatica estaticaJSON:estaticas){
			Estatica estatica = estaticaDao.get(estaticaJSON.getId());
			estatica.setTitulo(estaticaJSON.getTitulo());
		}
	}
}
