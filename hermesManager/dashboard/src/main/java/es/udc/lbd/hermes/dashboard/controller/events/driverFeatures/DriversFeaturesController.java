package es.udc.lbd.hermes.dashboard.controller.events.driverFeatures;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;


@RestController
@RequestMapping(value = "/events/driverFeatures")
public class DriversFeaturesController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(DriversFeaturesController.class);

	@Autowired
	private DriverFeaturesService driverFeaturesServicio;

	@RequestMapping(value="/json/eventsByUsuario", method = RequestMethod.GET)
	public List<DriverFeatures> getDriverFeaturess(@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		return driverFeaturesServicio.obterDriverFeaturessSegunUsuario(idUsuario);

	}
	
	@RequestMapping(value="/json/driversFeatures", method = RequestMethod.GET)
	public List<DriverFeatures> getDriverFeaturess() {
		return driverFeaturesServicio.obterDriverFeaturess();

	}

}
