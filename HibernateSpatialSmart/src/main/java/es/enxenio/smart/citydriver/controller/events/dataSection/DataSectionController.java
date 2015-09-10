package es.enxenio.smart.citydriver.controller.events.dataSection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
import es.enxenio.smart.citydriver.model.events.dataSection.service.DataSectionService;


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
