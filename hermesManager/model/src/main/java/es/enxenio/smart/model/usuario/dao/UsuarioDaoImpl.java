package es.enxenio.smart.model.usuario.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.model.usuario.Usuario;
import es.enxenio.smart.model.util.dao.GenericDaoHibernate;


@Repository
public class UsuarioDaoImpl extends GenericDaoHibernate<Usuario, Long> implements
UsuarioDao {
	@Override
	public List<Usuario> obterUsuarios() {

		List<Usuario> elementos = null;
		try {
			return getSession().createCriteria(this.entityClass).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	public Usuario findBySourceId(String sourceId) {

		try {
			return (Usuario) getSession().createCriteria(this.entityClass).add(Restrictions.eq("sourceId", sourceId)).setMaxResults(1).uniqueResult();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
}
