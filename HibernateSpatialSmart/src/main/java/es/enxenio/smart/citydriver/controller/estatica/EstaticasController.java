package es.enxenio.smart.citydriver.controller.estatica;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.estatica.service.EstaticaService;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEstatica;
import es.enxenio.smart.citydriver.web.rest.events.MainResource;


@RestController
@RequestMapping(value = "/estatica")
public class EstaticasController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(EstaticasController.class);

	@Autowired
	private EstaticaService estaticaService;
		
	@RequestMapping(value="/json/estaticas", method = RequestMethod.GET)
	public List<Estatica> getEstaticas() {
		return estaticaService.obterEstaticas();

	}
	
	@RequestMapping(value="/json/getEstatica", method = RequestMethod.GET)
	public Estatica getEstatica(@RequestParam(required = true) Long idEstatica) {		
		return estaticaService.get(idEstatica);

	}
		
	@RequestMapping(value = "/crearEstatica", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void crearEstatica(
			@RequestBody(required = true) JSONEstatica estatica,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			estaticaService.crearEstatica(estatica);
		} catch (Exception e) {
			log.error("Error al crear estatica: ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/borrarEstatica", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void borrarEstatica(
			@RequestBody(required = true) Long id,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			estaticaService.delete(id);
		} catch (Exception e) {
			log.error("Error al borrar estatica: ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/editarEstatica", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void editarEstatica(
			@RequestBody(required = true)JSONEstatica estatica,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {			
			estaticaService.editarEstatica(estatica);
		} catch (Exception e) {
			log.error("Error al editar estatica : ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/actualizarTitulosDeEstaticas", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void actualizarNombresDeEstaticas(
			@RequestBody(required = false) List<JSONEstatica> estaticas,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {			
			estaticaService.actualizarTitulosDeEstaticas(estaticas);
		} catch (Exception e) {
			log.error("Error al actualizar nombres de estaticas : ", e);
			throw e;
		}
	}

}
