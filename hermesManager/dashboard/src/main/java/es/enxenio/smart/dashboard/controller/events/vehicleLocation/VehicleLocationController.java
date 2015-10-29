package es.enxenio.smart.dashboard.controller.events.vehicleLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.enxenio.smart.model.events.vehicleLocation.service.VehicleLocationService;

@Controller
@RequestMapping("/events/vehicleLocation")
public class VehicleLocationController {

	@Autowired
	private VehicleLocationService vehicleLocationService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/listar")
	public void listar(
			ModelMap model) {
	}
}
