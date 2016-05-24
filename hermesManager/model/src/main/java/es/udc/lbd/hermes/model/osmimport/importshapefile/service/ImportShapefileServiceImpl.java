package es.udc.lbd.hermes.model.osmimport.importshapefile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.hermes.model.osmimport.importshapefile.ImportShapefile;

@Service("importShapefileService")
@Transactional
public class ImportShapefileServiceImpl implements ImportShapefileService{

	public void importar(ImportShapefile importacion, MultipartFile file){
		
	}
}
