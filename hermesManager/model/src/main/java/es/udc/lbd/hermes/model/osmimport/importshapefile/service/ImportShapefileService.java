package es.udc.lbd.hermes.model.osmimport.importshapefile.service;

import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.hermes.model.osmimport.importshapefile.ImportShapefile;

public interface ImportShapefileService {

	public void importar(ImportShapefile importacion, MultipartFile file);
}
