package es.udc.lbd.hermes.model.setting.dao;

import java.util.List;

import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface SettingDao extends GenericDao<Setting, Long>{

	public List<Setting> obterSettings();
	public Setting getByName(String name);
}
