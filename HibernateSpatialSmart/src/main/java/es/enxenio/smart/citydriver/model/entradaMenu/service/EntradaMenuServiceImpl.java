package es.enxenio.smart.citydriver.model.entradaMenu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.entradaMenu.dao.EntradaMenuDao;
import es.enxenio.smart.citydriver.model.usuario.dao.UsuarioDao;


@Service("entradaMenuService")
@Transactional
public class EntradaMenuServiceImpl implements EntradaMenuService {
	
	@Autowired
	private EntradaMenuDao entradaMenuDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public EntradaMenu get(Long id) {
		return entradaMenuDao.get(id);
	}

	@Override
	public void update(EntradaMenu entradaMenu) {
		entradaMenuDao.update(entradaMenu);
	}

	@Override
	public void delete(Long id) {
		EntradaMenu entradaMenu = entradaMenuDao.get(id);
		if (entradaMenu != null) {
			entradaMenuDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<EntradaMenu> obterEntradaMenus() {
		List<EntradaMenu> entradaMenus = entradaMenuDao.obterEntradaMenus();
		return entradaMenus;
	}
	
	@Transactional(readOnly = true)
	public List<EntradaMenu> obterEntradaMenusByMenuId(Long menuId) {
		List<EntradaMenu> entradaMenus = entradaMenuDao.obterEntradaMenusByMenuId(menuId);
		return entradaMenus;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Long obtenerSiguienteId() {
		return entradaMenuDao.obtenerSiguienteId();
	}
	
}
