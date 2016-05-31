package es.udc.lbd.hermes.eventManager.controller.osmimport.importshapefile;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

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
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.importshapefile.service.ImportShapefileService;
import es.udc.lbd.hermes.model.util.exceptions.DatosShapefileException;
import es.udc.lbd.hermes.model.util.exceptions.TablaExisteException;
import es.udc.lbd.hermes.model.util.exceptions.ZipShapefileException;

@RestController
@RequestMapping(value = "/api/importshapefile")
public class ImportShapefileController extends MainResource{

	static Logger logger = Logger.getLogger(ImportShapefileController.class);

	@Autowired private MessageSource messageSource;
	@Autowired
	private ImportShapefileService importShapefileServicio;
		
	@RequestMapping(value="/json/charsets", method = RequestMethod.GET)
	public List<Charset> getCharsets() { 
		SortedMap<String,Charset> map = importShapefileServicio.recuperarCharsetPosibles();
		
		List<Charset> charsets = new ArrayList<Charset>();
		for(Map.Entry<String,Charset> entry : map.entrySet()) {
			Charset charset = entry.getValue();
			charsets.add(charset);
		}
		
		return charsets;
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public JSONDataType importar(
			@RequestParam(value = "dbConnection", required = true) Long dbConnection,
			@RequestParam(value = "dbConcept", required = false) Long dbConcept,
			@RequestParam(value = "dbConceptName", required = false) String dbConceptName,
			@RequestParam(value = "dbConceptSchema", required = false) String dbConceptSchema,
			@RequestParam(value = "keepExistingData", required = true) Boolean keepExistingData,
			@RequestParam(value = "charset", required = true) Charset charset,
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONDataType jsonD = new JSONDataType();
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		
		try {
			if (dbConceptName.equals("null"))
				dbConceptName = null;
			
			if (dbConceptSchema.equals("null"))
				dbConceptSchema = null;
			
			importShapefileServicio.importar(dbConnection, dbConcept, dbConceptName, dbConceptSchema, keepExistingData, charset, file);
			
			String mensaje = messageSource.getMessage("importar.ok", null, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("info");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("importar.classnotfound", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
		} catch (SQLException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("importar.sqlexception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
			
		} catch (ZipShapefileException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getNombre()};
			String mensaje = messageSource.getMessage("importar.zipshapefileexception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
			
		} catch (TablaExisteException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getEsquema(), e.getNombre()};
			String mensaje = messageSource.getMessage("importar.tablaexisteexception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
		
		} catch (DatosShapefileException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {};
			String mensaje = messageSource.getMessage("importar.datosshapefileexception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("importar.exception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("importar.exception", parametros, locale);
			jsonD.setValue(mensaje);
			jsonD.setType("error");
		}

		return jsonD;
		
	}
}
