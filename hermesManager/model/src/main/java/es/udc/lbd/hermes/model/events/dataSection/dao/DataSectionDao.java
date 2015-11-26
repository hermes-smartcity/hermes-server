package es.udc.lbd.hermes.model.events.dataSection.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DataSectionDao extends GenericDao<DataSection, Long> {	
	public List<DataSection> obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count);
	public long contar();
}
