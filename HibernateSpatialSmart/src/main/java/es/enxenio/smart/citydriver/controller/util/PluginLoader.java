package es.enxenio.smart.citydriver.controller.util;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import es.enxenio.smart.citydriver.EventManager;

/**
 * Clase que carga a nivel de aplicaciÃ³n unha serie de obxectos, cando arranca a
 * aplicaciÃ³n. Os obxectos son accesibles nas jsp no scope applicationScope.
 * 
 * @author JosÃ© Luis - 09/11/2009
 */
public class PluginLoader implements ServletContextAware {
	
	private Logger log = LoggerFactory.getLogger(PluginLoader.class);
	private static final String EVENT_MANAGER = "EVENT_MANAGER";
	
	@Autowired private SessionLocaleResolver localeResolver;
		
	private static ServletContext contexto;
	public void setServletContext(ServletContext context) {
		contexto = context;
		init();
	}

	private void init(){
		EventManager eventManager = new EventManager();
		contexto.setAttribute(EVENT_MANAGER, eventManager);
		
//		contexto.setAttribute(NucleoConstantesController.Configuracion.CONFIGURACION_MODULOS_DB, configuracionService.getParametrosConfiguracion());
		
//		ConfiguracionLinguaxes configuracionLinguaxes = linguaxeService.getConfiguracionLinguaxes();
		// Establecer o locale por defecto para a aplicaciÃ³n
//		localeResolver.setDefaultLocale(new Locale(configuracionLinguaxes.getPorDefecto().getId()));
//		contexto.setAttribute(NucleoConstantesController.Configuracion.CONFIGURACION_LINGUAXES, configuracionLinguaxes);
		
//		menuHelper.cargarMenu(contexto);
		
		
		
	}
	

	

}
