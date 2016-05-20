package es.udc.lbd.hermes.model.setting.service;

import java.util.List;

import es.udc.lbd.hermes.model.setting.Setting;

public interface SettingService {

	public Setting get(Long id);
	
	public void update(Setting setting);
	
	public void updateSettings(List<Setting> settings);
	
	public List<Setting> obterSettings();
	
	public Setting getByName(String name);
	
}
