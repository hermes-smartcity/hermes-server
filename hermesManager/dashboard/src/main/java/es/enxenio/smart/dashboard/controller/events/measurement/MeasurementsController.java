package es.enxenio.smart.dashboard.controller.events.measurement;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.enxenio.smart.dashboard.web.rest.events.MainResource;
import es.enxenio.smart.model.events.measurement.Measurement;
import es.enxenio.smart.model.events.measurement.MeasurementType;
import es.enxenio.smart.model.events.measurement.service.MeasurementService;;

@RestController
@RequestMapping(value = "/events/measurement")
public class MeasurementsController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(MeasurementsController.class);

	@Autowired
	private MeasurementService measurementServicio;

	@RequestMapping(value="/json/measurementsByUsuario", method = RequestMethod.GET)
	public List<Measurement> getMeasurements(@RequestParam(required = true) String tipo,
			@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		return  measurementServicio.obterMeasurementsSegunTipoEusuario(MeasurementType.getTipo(tipo), idUsuario);

	}
	
	@RequestMapping(value="/json/measurements", method = RequestMethod.GET)
	public List<Measurement> getMeasurements(@RequestParam(required = true) String tipo) {
		return measurementServicio.obterMeasurementsSegunTipo(MeasurementType.getTipo(tipo));

	}

}
