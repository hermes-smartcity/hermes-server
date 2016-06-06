package es.udc.lbd.hermes.model.usuario.usuarioMovil.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.FechaUtil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class UsuarioMovilDaoImpl extends GenericDaoHibernate<UsuarioMovil, Long> implements
UsuarioMovilDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<UsuarioMovil> obterUsuariosMovil() {
		try {
			return getSession().createCriteria(this.entityClass).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	public UsuarioMovil findBySourceId(String sourceId) {

		try {
			return (UsuarioMovil) getSession().createCriteria(this.entityClass).add(Restrictions.eq("sourceId", sourceId)).setMaxResults(1).uniqueResult();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from UsuarioMovil")
				.uniqueResult();
	}
	
	//	Número de usuarios_movil con eventos registrados en la última semana
	@Override
	public long getNumberActiveUsers(){
	
				// Se supone que no vamos a tener eventos guardados posteriores a la fecha de ahora mismo por supuesto. 
				// Por lo tanto no se limita la fechaHasta 
				Calendar dateHasta = FechaUtil.getHoy();				
				Calendar fechaDesde = FechaUtil.getAnteriorSemana(dateHasta);
				
				String queryStr =  "select count(*) from UsuarioMovil u where u.id in (select distinct v.usuarioMovil.id from VehicleLocation v where ";
				
				if(fechaDesde!=null)
					queryStr += " v.timestamp > :fechaDesde)";
				
//				if(dateHasta!=null)
//					queryStr += "and v.timestamp < :dateHasta)";
				
				Query query = getSession().createQuery(queryStr);
		
				if(fechaDesde!=null)
					 query.setCalendar("fechaDesde", fechaDesde);
//				if(dateHasta!=null)
//					query.setCalendar("dateHasta", dateHasta);
				
				return (long) query.uniqueResult();
			
	}
}
