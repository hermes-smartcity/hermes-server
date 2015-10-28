package es.enxenio.smart.model.usuario.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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

		Usuario usuario = null;
		try {
			return (Usuario) getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.sourceId", sourceId)).setMaxResults(1).uniqueResult();
//			return (Usuario) getSession().createCriteria(this.entityClass).setMaxResults(1).uniqueResult();
//			usuario = (Usuario) getSession()
//					.createQuery("from Usuario x where sourceId = :sourceId")
//					.setString("sourceId", sourceId).uniqueResult();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
}
