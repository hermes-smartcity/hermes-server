package es.udc.lbd.hermes.model.usuario.usuarioMovil.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class UsuarioMovilDaoImpl extends GenericDaoHibernate<UsuarioMovil, Long> implements
UsuarioMovilDao {
	@Override
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
}
