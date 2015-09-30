package es.enxenio.smart.citydriver.model.entradaMenu.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

@Repository
public class EntradaMenuDaoImpl extends GenericDaoHibernate<EntradaMenu, Long> implements
EntradaMenuDao {
	
	@Override
	public List<EntradaMenu> obterEntradaMenus() {
		Order order = Order.asc("orden");
		Criteria criteria = getSession().createCriteria(this.entityClass);
		criteria.addOrder(order);
		return criteria.list();
	}
	
	@Override
	public List<EntradaMenu> obterEntradaMenusByMenuId(Long idMenu) {
		Order order = Order.asc("orden");
		Criteria criteria = getSession().createCriteria(this.entityClass).add(Restrictions.eq("menu.id", idMenu));
		criteria.addOrder(order);
		return criteria.list();
	}
	
	@Override
	public void create(EntradaMenu entradaMenu, Menu menu, EntradaMenu entradaMenuPadre) {
		entradaMenu.setMenu(menu);
		entradaMenu.setEntradaMenuPadre(entradaMenuPadre);
		super.create(entradaMenu);
	}
	
	
	private int getSeguienteOrden(EntradaMenu entradaMenu) {
		
		String queryS = "select coalesce(max(orden), 0) + 1 from EntradaMenu";
		if(entradaMenu.getEntradaMenuPadre()!=null)
			queryS += "where idEntradaMenuPadre = :idEntradaMenuPadre";
		
		Query query =  getSession().createQuery(queryS);
		
		//TODO no estoy segura de que vaya a ser asi
		if(entradaMenu.getEntradaMenuPadre()!=null)
			query.setParameter("idEntradaMenuPadre", entradaMenu.getEntradaMenuPadre().getId());
			
		return (Integer) query.uniqueResult();
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
