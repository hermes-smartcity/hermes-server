package es.udc.lbd.hermes.dashboard.controller.events.measurement;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;

@RestController
@RequestMapping(value = "/events/measurement")
public class MeasurementsController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(MeasurementsController.class);

	@Autowired
	private MeasurementService measurementServicio;
	
	@RequestMapping(value="/json/measurements", method = RequestMethod.GET)
	public List<Measurement> getMeasurements(@RequestParam(required = true) MeasurementType tipo,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario,			
			@RequestParam(value = "fechaIni", required = false) String fechaIni,
			@RequestParam(value = "fechaFin", required = false) String fechaFin,
			@RequestParam(value = "wnLng", required = true) Double wnLng,
			@RequestParam(value = "wnLat", required = true) Double wnLat,
			@RequestParam(value = "esLng", required = true) Double esLng, 
			@RequestParam(value = "esLat", required = true) Double esLat) {

		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		
		return measurementServicio.obterMeasurementsSegunTipo(tipo, idUsuario, ini, fin,
				wnLng, wnLat,esLng, esLat);

	}

}
