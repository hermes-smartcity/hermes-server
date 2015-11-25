package es.udc.lbd.hermes.dashboard.controller.events.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;

@Controller
@RequestMapping("/events/dashboard")
public class DashboardController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/mostrar")
	public void mostrar(
			ModelMap model) {
	}
}
