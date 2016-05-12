package es.udc.lbd.hermes.model.osmimport.message.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class MessageDaoImpl extends GenericDaoHibernate<Message, Long> implements MessageDao{

	@SuppressWarnings("unchecked")
	public List<Message> getAll(Long idExecution){
		try {
			return getSession().createCriteria(this.entityClass).
					add(Restrictions.eq("execution.id", idExecution)).
					addOrder(Order.asc("timestamp")).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}

}
