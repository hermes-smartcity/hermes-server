package es.udc.lbd.hermes.dashboard.controller.publica;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("principalController")
public class PrincipalController {

	@RequestMapping("/")
	public String inicio(HttpSession session) {				
		return "redirect:paxinaInicio/inicio";
	}

}
