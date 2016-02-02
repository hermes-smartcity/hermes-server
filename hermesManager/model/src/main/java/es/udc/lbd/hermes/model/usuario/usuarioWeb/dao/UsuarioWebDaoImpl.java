package es.udc.lbd.hermes.model.usuario.usuarioWeb.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class UsuarioWebDaoImpl extends GenericDaoHibernate<UsuarioWeb, Long> implements
UsuarioWebDao {
	@Override
	public List<UsuarioWeb> obterUsuariosWeb() {
		try {
			return getSession().createCriteria(this.entityClass).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	public UsuarioWeb findBySourceId(String sourceId) {

		try {
			return (UsuarioWeb) getSession().createCriteria(this.entityClass).add(Restrictions.eq("sourceId", sourceId)).setMaxResults(1).uniqueResult();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}				
	}
	
	public List<UsuarioWeb> obterUsuariosWebSegunRol(Rol rol){

		try {
			return  getSession().createCriteria(this.entityClass).add(Restrictions.eq("rol", rol)).list();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
	
	public UsuarioWeb findByEmail(String email) {
		try {
			return (UsuarioWeb) getSession().createCriteria(this.entityClass).add(Restrictions.eq("email", email)).setMaxResults(1).uniqueResult();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}
	
	
}
