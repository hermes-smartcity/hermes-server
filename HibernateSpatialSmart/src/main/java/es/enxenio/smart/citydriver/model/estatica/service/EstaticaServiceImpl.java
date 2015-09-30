package es.enxenio.smart.citydriver.model.estatica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.estatica.dao.EstaticaDao;
import es.enxenio.smart.citydriver.model.usuario.dao.UsuarioDao;


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
	public void update(Estatica estatica) {
		estaticaDao.update(estatica);
	}

	@Override
	public void delete(Long id) {
		Estatica estatica = estaticaDao.get(id);
		if (estatica != null) {
			estaticaDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<Estatica> obterEstaticas() {
		List<Estatica> estaticas = estaticaDao.obterEstaticas();
		return estaticas;
	}
}
