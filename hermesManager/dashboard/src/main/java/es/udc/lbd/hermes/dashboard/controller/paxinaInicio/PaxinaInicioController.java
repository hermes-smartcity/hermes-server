package es.udc.lbd.hermes.dashboard.controller.paxinaInicio;


import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paxinaInicio")
public class PaxinaInicioController {	
	
	@RequestMapping("/inicio")
	public void listar(HttpSession session) {
//		// Se carga en session sino existe el eventManager. 
//		if(SessionManager.getEventManager(session)==null){
//			SessionManager.crearEventManager(session);
//			System.out.println("Creado un event manager en su session");
//		}
//		
//		// TODO anadir a log y un error en session manager
//		else System.out.println("Ya tiene cargado en session manager un event manager");
		
	}
}
