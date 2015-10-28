package es.enxenio.smart.model.events.dataSection.dao;

import java.util.List;

import es.enxenio.smart.model.events.dataSection.DataSection;
import es.enxenio.smart.model.util.dao.GenericDao;



public interface DataSectionDao extends GenericDao<DataSection, Long> {	
	public List<DataSection> obterDataSections();
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario);
}
