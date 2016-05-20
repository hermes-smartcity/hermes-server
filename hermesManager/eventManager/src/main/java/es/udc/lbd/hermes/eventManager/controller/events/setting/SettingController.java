package es.udc.lbd.hermes.eventManager.controller.events.setting;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONDataType;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.service.SettingService;

@RestController
@RequestMapping(value = "/api/setting")
public class SettingController extends MainResource {

	static Logger logger = Logger.getLogger(SettingController.class);

	@Autowired
	private SettingService settingService;

	// Listar settings - Sólo administradores
	@RequestMapping(value = "/json/settings", method = RequestMethod.GET)
	public List<Setting> getSettings() {
		return settingService.obterSettings();
	}
	
	// Actualizar settings
	@RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
	public JSONDataType changePassword(@RequestBody List<Setting> settings) {
		JSONDataType jsonD = new JSONDataType();

		settingService.updateSettings(settings);
		jsonD.setKey("settings.updateOk"); //corresponde con el translation_xx.js
		jsonD.setType("info");
		logger.info("Setting actualizado correctamente correctamente");

		return jsonD;
	}
	
	@RequestMapping(value = "/json/getSetting", method = RequestMethod.GET)
	public Setting getSetting(@RequestParam(value = "id", required = true) Long id) {
		return settingService.get(id);

	}
}
