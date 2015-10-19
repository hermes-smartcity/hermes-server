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
		// Se controla en angular, pero por si nos mandan algo con inpeccionar y empezamos a borrar cosas que no se deben o nos mandan null y casca
		if(id!=null){
			Menu menu = menuDao.get(id);
			if (menu != null) {
				menuDao.delete(id);
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Menu> obterMenus() {
		List<Menu> menus = menuDao.obterMenus();
		return menus;
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
		Integer orden = 0;
		for(JSONEntradaMenu jsonE:menuJSON.getEntradasMenu()){			
			guardarEntradaMenu(jsonE, menu, orden);
			orden++;
		}

		return menu;
	}

	public EntradaMenu guardarEntradaMenu(JSONEntradaMenu entradaMenuJSON, Menu menu, Integer orden){
		EntradaMenu entradaMenu = new EntradaMenu();
		setearValoresEntradaMenuJSON(entradaMenu, entradaMenuJSON, orden);
	
		// Estamos creando las entradas del primer nivel por lo tanto no tienen entrada padre
		entradaMenuDao.create(entradaMenu, menu, null);
		
		// Ahora cada una de las hijas de la entrada menu, tendra como padre esta y asi recursivamente
		guardaEntradasMenuHijas(menu, entradaMenu, entradaMenuJSON.getEntradasMenu());		
		
		return entradaMenu;
	}

	public void guardaEntradasMenuHijas(Menu menu, EntradaMenu entradaMenuPadre, List<JSONEntradaMenu> entradasMenuHijas){
		List<EntradaMenu> entradasMenu = new ArrayList<EntradaMenu>();
		Integer ordenHija = 0;
		// Tengo que crear sus entradasMenu, asi como sus hijas -> recursivo
		for(JSONEntradaMenu entradaHija:entradasMenuHijas){
			EntradaMenu e = new EntradaMenu();
			e.setTexto(entradaHija.getTexto());
			e.setUrl(entradaHija.getUrl());
			e.setOrden(ordenHija);
			e.setEntradaMenuPadre(entradaMenuPadre);
			entradaMenuDao.create(e);
			guardaEntradasMenuHijas(menu,e,entradaHija.getEntradasMenu());
			entradasMenu.add(e);
			ordenHija++;
		}
		
		entradaMenuPadre.setEntradasMenu(entradasMenu);
		entradaMenuDao.save(entradaMenuPadre);
		return;
	}
	
	public void setearValoresEntradaMenuJSON(EntradaMenu entradaMenu, JSONEntradaMenu entradaMenuJSON, Integer orden){		
		entradaMenu.setTexto(entradaMenuJSON.getTexto());
		entradaMenu.setUrl(entradaMenuJSON.getUrl());
		entradaMenu.setOrden(orden);
		
	}
	
	public void actualizarNombresDeMenus(List<JSONMenu> menus){
		for(JSONMenu menuJSON:menus){
			Menu menu = menuDao.get(menuJSON.getId());
			menu.setNombre(menuJSON.getNombre());
		}
	}
	
	public void editarMenu(JSONMenu menuJson) throws InstanceNotFoundException{		
		Menu menu = menuDao.get(menuJson.getId());
		// Por si modifican con el inspeccionar o algo el id y me mandan algo erróneo...
		if(menu!=null){
			menu.setNombre(menuJson.getNombre());			
			// Actualizamos sus entradas de menu
			actualizarEntradasMenu(menuJson.getEntradasMenu(), menu);
		}
	}
	
	public void actualizarEntradasMenu(List<JSONEntradaMenu> entradasMenu, Menu menu){
		// Le asigno las entradas guardadas en el JSON
		Integer orden = 0;
		for(JSONEntradaMenu jsonE:entradasMenu){			
			actualizarEntradaMenu(jsonE, menu, orden);
			orden++;
		}

	}
	
	public EntradaMenu actualizarEntradaMenu(JSONEntradaMenu entradaMenuJSON, Menu menu, Integer orden){
		boolean crearEntradaNueva = false;
		EntradaMenu entradaMenu = null;
		 
		if(entradaMenuJSON.getId()!=null)
			entradaMenu = entradaMenuDao.get(entradaMenuJSON.getId());
		
		// La entrada de menu se ha creado nueva
		if(entradaMenu == null){
			entradaMenu = new EntradaMenu();
			crearEntradaNueva = true;
		}		
		
		setearValoresEntradaMenuJSON(entradaMenu, entradaMenuJSON, orden);
	
		// Estamos creando las entradas del primer nivel por lo tanto no tienen entrada padre
		if(crearEntradaNueva)
			entradaMenuDao.create(entradaMenu, menu, null);
		
		// Ahora cada una de las hijas de la entrada menu, tendra como padre esta y asi recursivamente
		actualizarEntradasMenuHijas(menu, entradaMenu, entradaMenuJSON.getEntradasMenu());		
		
		return entradaMenu;
	}

	public void actualizarEntradasMenuHijas(Menu menu, EntradaMenu entradaMenuPadre, List<JSONEntradaMenu> entradasMenuHijas){
		List<EntradaMenu> entradasMenu = new ArrayList<EntradaMenu>();
		Integer ordenHija = 0;
		// Tengo que crear sus entradasMenu, asi como sus hijas -> recursivo
		for(JSONEntradaMenu entradaHija:entradasMenuHijas){
			boolean crearEntradaNueva = false;
			EntradaMenu e = null;
			if(entradaHija.getId()!=null)
				e = entradaMenuDao.get(entradaHija.getId());
			// La entrada de menu se ha creado nueva 			
			if(e == null){
				e = new EntradaMenu();
				crearEntradaNueva = true;
			}		
			e.setTexto(entradaHija.getTexto());
			e.setUrl(entradaHija.getUrl());
			e.setOrden(ordenHija);
			e.setEntradaMenuPadre(entradaMenuPadre);
			if(crearEntradaNueva)
				entradaMenuDao.create(e);
			actualizarEntradasMenuHijas(menu,e,entradaHija.getEntradasMenu());
			entradasMenu.add(e);
			ordenHija++;
		}
		
		entradaMenuPadre.setEntradasMenu(entradasMenu);
		entradaMenuDao.save(entradaMenuPadre);
		return;
	}
	
	
}
