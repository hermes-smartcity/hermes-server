package es.udc.lbd.hermes.model.events.dataSection.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;



@Repository
public class DataSectionDaoImpl extends GenericDaoHibernate<DataSection, Long> implements
DataSectionDao {
	
	@Override
	public List<DataSection> obterDataSections() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DataSection> obterDataSectionsByBounds(Geometry bounds) {
		List<DataSection> elementos = null;

		String queryStr =  "from DataSection where within(roadSection, :bounds) = true";
		
		Query query = getSession().createQuery(queryStr);

		elementos = query.setParameter("bounds", bounds).list();
		return elementos;
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
