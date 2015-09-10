package es.enxenio.smart.citydriver.model.util.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoHibernate<E, PK extends Serializable> implements
		GenericDao<E, PK> {

	@Autowired
	private SessionFactory sessionFactory;
	protected Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDaoHibernate() {
		this.entityClass = (Class<E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void create(E entity) {
		getSession().persist(entity);
	}

	public boolean exists(PK id) {
		boolean exists = getSession().createCriteria(entityClass)
				.add(Restrictions.idEq(id)).setProjection(Projections.id())
				.uniqueResult() != null;
		return exists;
	}

	@SuppressWarnings("unchecked")
	public E get(PK id) {
		return (E) getSession().get(entityClass, id);
	}

	public void delete(PK id) {
		getSession().delete(get(id));
	}

	public void update(E entity) {
		getSession().merge(entity);
	}
	
	public void save(E entity) {
		getSession().saveOrUpdate(entity);
	}

}
