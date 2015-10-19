package es.enxenio.smart.citydriver.model.menu.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

@Repository
public class MenuDaoImpl extends GenericDaoHibernate<Menu, Long> implements
MenuDao {
	
	@Override
	public List<Menu> obterMenus() {		
		Order order = Order.asc("nombre");
		Criteria criteria = getSession().createCriteria(this.entityClass);
		criteria.addOrder(order);
		return criteria.list();
	}


}
