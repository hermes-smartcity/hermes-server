package es.udc.lbd.hermes.model.setting.dao;

import java.util.List;

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
}
