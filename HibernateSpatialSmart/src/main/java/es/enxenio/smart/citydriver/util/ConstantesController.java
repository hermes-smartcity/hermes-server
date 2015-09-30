package es.enxenio.smart.citydriver.util;

public class ConstantesController {
	
	public ConstantesController() {}
	
	// -------------------------------------------------------------------------
	// PREFIJOS
	// -------------------------------------------------------------------------
	
	public static final String PREFIJO_URL_REST = "/rest/1-2-0";
	public static final String PREFIJO_URL_PARTES_MENU_RESPONSABLE =  "/menu/partes";
	
	// -------------------------------------------------------------------------
	// URLs LOGIN
	// -------------------------------------------------------------------------
	public static final String URL_LOGIN = "/usuario/login.htm";
	
	// -------------------------------------------------------------------------
	// URLs Páginas 'Usuario'
	// -------------------------------------------------------------------------
	
	public static final String URL_USUARIO_NOVO_CONTRASINAL = "/usuario/novoContrasinal.htm";
	public static final String URL_INDICE = "/index.htm";
	

	// -------------------------------------------------------------------------
	// APP 'Menu'
	// -------------------------------------------------------------------------
	
	// URL Página principal
	public static final String URL_MENU = "/menu.htm";
	
	// URLs Partes 'Menu'
	
	public static final String PARTE_MENU = "/centro/centro.htm";
	public static final String PARTE_MENU_LISTAR = "/menu/listar.htm";
	public static final String PARTE_MENU_CREAR = "/menu/crear.htm";
	// -------------------------------------------------------------------------
	// URLs REST 'Menus'
	// -------------------------------------------------------------------------
	
	public static final String URL_MENUS = PREFIJO_URL_REST + "/menus";
	
	// -------------------------------------------------------------------------
	// Cacheado
	// -------------------------------------------------------------------------
	
	public static final int UN_MES = 30 * 24 * 60 * 60;
	
	// -------------------------------------------------------------------------
	// Paginación
	// -------------------------------------------------------------------------
	public static final int NUMERO_REGISTROS_PAGINA = 50; 
	public static final int NUMERO_PERSONAS_PAGINA = 50;

}
