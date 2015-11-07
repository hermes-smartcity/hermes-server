package es.udc.lbd.hermes.dashboard.controller.events.dataSection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;

@Controller
@RequestMapping("/events/dataSection")
public class DataSectionController {

	@Autowired
	private DataSectionService dataSectionService;
	private Logger logger = LoggerFactory.getLogger(getClass());
		
	@RequestMapping("/listar")
	public void listar(
			ModelMap model) {
	}
}
