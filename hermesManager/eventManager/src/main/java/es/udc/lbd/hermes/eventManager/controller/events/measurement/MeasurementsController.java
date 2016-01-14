package es.udc.lbd.hermes.eventManager.controller.events.measurement;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/measurement")
public class MeasurementsController extends MainResource {

	static Logger logger = Logger.getLogger(MeasurementsController.class);
	@Autowired
	private MeasurementService measurementServicio;
	
	@RequestMapping(value="/json/measurements", method = RequestMethod.GET)
	public List<Measurement> getMeasurements(@RequestParam(required = true) MeasurementType tipo,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario,			
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin,
			@RequestParam(value = "wnLng", required = true) Double wnLng,
			@RequestParam(value = "wnLat", required = true) Double wnLat,
			@RequestParam(value = "esLng", required = true) Double esLng, 
			@RequestParam(value = "esLat", required = true) Double esLat) {

		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		return measurementServicio.obterMeasurementsSegunTipo(tipo, idUsuario, ini, fin,
				wnLng, wnLat,esLng, esLat);

	}
	
	@RequestMapping(value="/json/eventosPorDia", method = RequestMethod.GET)
	public ListaEventosYdias getEventosPorDia(
			@RequestParam(required = true) MeasurementType tipo,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) {
		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		return measurementServicio.obterEventosPorDia(tipo, idUsuario, ini, fin);
		
	}

}
