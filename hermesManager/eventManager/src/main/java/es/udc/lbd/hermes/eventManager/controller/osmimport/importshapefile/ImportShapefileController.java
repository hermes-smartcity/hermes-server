package es.udc.lbd.hermes.eventManager.controller.osmimport.importshapefile;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.hermes.eventManager.controller.util.JSONDataType;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.events.MainResource;
import es.udc.lbd.hermes.model.osmimport.importshapefile.ImportShapefile;
import es.udc.lbd.hermes.model.osmimport.importshapefile.service.ImportShapefileService;

@RestController
@RequestMapping(value = "/api/importshapefile")
public class ImportShapefileController extends MainResource{

	static Logger logger = Logger.getLogger(ImportShapefileController.class);

	@Autowired private MessageSource messageSource;
	@Autowired
	private ImportShapefileService importShapefileServicio;
		
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public JSONDataType importar(@RequestPart(value = "model", required = true) ImportShapefile model,
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONDataType jsonD = new JSONDataType();
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		
		importShapefileServicio.importar(model, file);

		return jsonD;
		
	}
}
