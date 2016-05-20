package es.udc.lbd.hermes.model.setting.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class SettingDaoImpl extends GenericDaoHibernate<Setting, Long> implements SettingDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Setting> obterSettings() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	public Setting getByName(String name){
		try {
			return  (Setting) getSession().createCriteria(this.entityClass).add(Restrictions.eq("name", name)).uniqueResult();

		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}
}
