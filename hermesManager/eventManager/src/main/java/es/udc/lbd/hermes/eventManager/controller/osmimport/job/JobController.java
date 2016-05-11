package es.udc.lbd.hermes.eventManager.controller.osmimport.job;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.osmimport.dbConcept.DBConceptController;
import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;
import es.udc.lbd.hermes.model.osmimport.job.service.JobService;


@RestController
@RequestMapping(value = "/api/job")
public class JobController extends MainResource{

static Logger logger = Logger.getLogger(DBConceptController.class);
	
	@Autowired private JobService jobServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/jobs", method = RequestMethod.GET)
	public List<Job> getJobs() { 

		return jobServicio.getAll();

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		jobServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedJobOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody JobDTO job,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		jobServicio.register(job);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdJobOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/json/job", method = RequestMethod.GET)
	public Job getJob(@RequestParam(value = "id", required = true) Long id) {
		return jobServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody JobDTO job,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		jobServicio.update(job, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedJobOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/executeJob", method = RequestMethod.POST)
	public Execution executeJob(@RequestParam(value = "id", required = true) Long id) {
		//Creamos la execution del job y devolvemos su id
		return jobServicio.createExecution(id);
	}
}
