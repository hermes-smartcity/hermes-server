package es.enxenio.smart.citydriver.model.menu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.entradaMenu.dao.EntradaMenuDao;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.menu.dao.MenuDao;
import es.enxenio.smart.citydriver.model.usuario.dao.UsuarioDao;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEntradaMenu;
import es.enxenio.smart.citydriver.web.rest.custom.JSONMenu;


@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private EntradaMenuDao entradaMenuDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public Menu get(Long id) {
		return menuDao.get(id);
	}

	@Override
	public void create(Menu menu) {			
		menuDao.create(menu);	
	}

	@Override
	public void update(Menu menu) {
		menuDao.update(menu);
	}

	@Override
	public void delete(Long id) {
		Menu menu = menuDao.get(id);
		if (menu != null) {
			menuDao.delete(id);
		}
	}

	@Transactional(readOnly = true)
	public List<Menu> obterMenus() {
		List<Menu> menus = menuDao.obterMenus();
		return menus;
	}

	@Override
	@Transactional(readOnly = false)
	public Long obtenerSiguienteId() {
		return menuDao.obtenerSiguienteId();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { InstanceNotFoundException.class })
	public void guardarMenu(JSONMenu menuJSON)
			throws InstanceNotFoundException {
		// TODO Duda para Alex: me había dicho que preferia que se lo guardase como JSON?¿ o le vale así?¿
		Menu menu = prepararMenu(menuJSON);
		menuDao.save(menu);
	}

	public Menu prepararMenu(JSONMenu menuJSON){
		// Creo el menu y le asigno los valores almacenados en el JSON
		Menu menu = new Menu();
		menu.setNombre(menuJSON.getNombre());
		// TODO no estoy segura de que sea la mejor forma de hacerlo, primero lo creo con los datos básicos, luego le asigno las relaciones con las demás entidades, una vez creado
		menuDao.create(menu);
		// Le asigno las entradas guardadas en el JSON
		for(JSONEntradaMenu jsonE:menuJSON.getEntradasMenu()){			
			guardarEntradaMenu(jsonE, menu);
		}

		return menu;
	}

	public EntradaMenu guardarEntradaMenu(JSONEntradaMenu entradaMenuJSON, Menu menu){
		EntradaMenu entradaMenu = setearValoresEntradaMenuJSON(entradaMenuJSON);
	
		// Estamos creando las entradas del primer nivel por lo tanto no tienen entrada padre
		entradaMenuDao.create(entradaMenu, menu, null);
		
		// Ahora cada una de las hijas de la entrada menu, tendra como padre esta y asi recursivamente
		guardaEntradasMenuHijas(menu, entradaMenu, entradaMenuJSON.getEntradasMenu());		
		
		return entradaMenu;
	}

	public void guardaEntradasMenuHijas(Menu menu, EntradaMenu entradaMenuPadre, List<JSONEntradaMenu> entradasMenuHijas){
		List<EntradaMenu> entradasMenu = new ArrayList<EntradaMenu>();
		// Tengo que crear sus entradasMenu, asi como sus hijas -> recursivo
		for(JSONEntradaMenu entradaHija:entradasMenuHijas){
			EntradaMenu e = new EntradaMenu();
			e.setTexto(entradaHija.getTexto());
			e.setUrl(entradaHija.getUrl());
			e.setOrden(entradaHija.getOrden());
			e.setEntradaMenuPadre(entradaMenuPadre);
			entradaMenuDao.create(e);
			guardaEntradasMenuHijas(menu,e,entradaHija.getEntradasMenu());
			entradasMenu.add(e);

		}
		
		entradaMenuPadre.setEntradasMenu(entradasMenu);
		entradaMenuDao.save(entradaMenuPadre);
		return;
	}
	
	public EntradaMenu setearValoresEntradaMenuJSON(JSONEntradaMenu entradaMenuJSON){
		EntradaMenu entradaMenu = new EntradaMenu();
		entradaMenu.setTexto(entradaMenuJSON.getTexto());
		entradaMenu.setUrl(entradaMenuJSON.getUrl());
		entradaMenu.setOrden(entradaMenuJSON.getOrden());
		return entradaMenu;
		
	}
	

}
