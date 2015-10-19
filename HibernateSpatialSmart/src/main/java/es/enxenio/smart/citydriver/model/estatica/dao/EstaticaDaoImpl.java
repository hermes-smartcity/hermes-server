package es.enxenio.smart.citydriver.model.estatica.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

@Repository
public class EstaticaDaoImpl extends GenericDaoHibernate<Estatica, Long> implements
EstaticaDao {
	
	@Override
	public List<Estatica> obterEstaticas() {
		Order order = Order.asc("titulo");
		Criteria criteria = getSession().createCriteria(this.entityClass);
		criteria.addOrder(order);
		return criteria.list();
	}
	
}
