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
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@Override
	public Long obtenerSiguienteId() {
		return (Long) getSession()
				.createSQLQuery("select nextval('id') as id")
				.addScalar("id", LongType.INSTANCE).uniqueResult();
	}

//	@Override
//	public void subir(long idMenu) {
//		Menu menu = get(idMenu);
//		Menu outro = (Menu) getSession()
//				.createQuery("from Menu where (capitulo = :capitulo and orde < :orde and idMenuPai is null) order by orde desc")
//				.setParameter("capitulo", menu.getCapitulo())
//				.setParameter("orde", menu.getOrden())
//				.setMaxResults(1)
//				.uniqueResult();
//		intercambiarOrde(menu, outro);
//	}
//
//	@Override
//	public void baixar(long idMenu) {
//		Menu menu = get(idMenu);
//		Menu outro = (Menu) getSession()
//				.createQuery("from Menu where (capitulo = :capitulo and orde > :orde and idMenuPai is null) order by orde asc")
//				.setParameter("capitulo", menu.getCapitulo())
//				.setParameter("orde", menu.getOrden())
//				.setMaxResults(1)
//				.uniqueResult();
//		intercambiarOrde(menu, outro);
//	}
//	
//	//para menu y submenu
//	private void intercambiarOrde(Menu menu, Menu outro) {
//		int outraOrde = outro.getOrden();
//		outro.setOrden(menu.getOrden());
//		menu.setOrden(0);
//		getSession().flush();
//		menu.setOrden(outraOrde);
//	}
}
