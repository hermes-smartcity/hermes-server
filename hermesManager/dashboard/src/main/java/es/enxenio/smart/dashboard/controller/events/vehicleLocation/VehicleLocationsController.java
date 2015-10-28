package es.enxenio.smart.dashboard.controller.events.vehicleLocation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.enxenio.smart.dashboard.web.rest.events.MainResource;
import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.model.events.vehicleLocation.service.VehicleLocationService;


@RestController
@RequestMapping(value = "/events/vehicleLocation")
public class VehicleLocationsController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(VehicleLocationsController.class);

	@Autowired
	private VehicleLocationService vehicleLocationServicio;

	@RequestMapping(value="/json/vehicleLocationsByUsuario", method = RequestMethod.GET)
	public List<VehicleLocation> getVehicleLocations(@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		return vehicleLocationServicio.obterVehicleLocationsSegunUsuario(idUsuario);

	}
	
	@RequestMapping(value="/json/vehicleLocations", method = RequestMethod.GET)
	public List<VehicleLocation> getVehicleLocations() {
		return vehicleLocationServicio.obterVehicleLocations();

	}

}
