package es.udc.lbd.hermes.model.osmimport.importshapefile.service;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.SortedMap;

import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.hermes.model.util.exceptions.DatosShapefileException;
import es.udc.lbd.hermes.model.util.exceptions.TablaExisteException;
import es.udc.lbd.hermes.model.util.exceptions.ZipShapefileException;

public interface ImportShapefileService {

	public SortedMap<String,Charset> recuperarCharsetPosibles();
	public void importar(Long dbConnection, Long dbConcept, String dbConceptName, String dbConceptSchema, Boolean keepExistingData, Charset charset, MultipartFile file)  throws ClassNotFoundException, SQLException, ZipShapefileException, TablaExisteException, DatosShapefileException, IllegalStateException, Exception;
}
