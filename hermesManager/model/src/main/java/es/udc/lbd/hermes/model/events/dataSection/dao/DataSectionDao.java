package es.udc.lbd.hermes.model.events.dataSection.dao;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DataSectionDao extends GenericDao<DataSection, Long> {	
	public List<DataSection> obterDataSections();
	public List<DataSection> obterDataSectionsByBounds(Geometry bounds);
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario);
}
