package es.udc.lbd.hermes.model.setting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;

@Service("settingService")
@Transactional
public class SettingServiceImpl implements SettingService{

	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public Setting get(Long id) {
		return settingDao.get(id);
	}
	
	@Override
	public void update(Setting setting) {
		settingDao.update(setting);
	}
	
	@Override
	public void updateSettings(List<Setting> settings) {
		for (Setting setting : settings) {
			settingDao.update(setting);	
		}
	}
	
	@Transactional(readOnly = true)
	public List<Setting> obterSettings() {
		List<Setting> settings = settingDao.obterSettings();
		return settings;
	}
}
