package es.enxenio.smart.citydriver.model.events.dataSection.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.measurement.Measurement;
import es.enxenio.smart.citydriver.model.events.measurement.MeasurementType;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

@Repository
public class DataSectionDaoImpl extends GenericDaoHibernate<DataSection, Long> implements
DataSectionDao {
	
	@Override
	public List<DataSection> obterDataSections() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@Override
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario) {
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
	
}
