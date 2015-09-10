package es.enxenio.smart.citydriver.controller.events.measurement;

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
import org.springframework.web.bind.annotation.ResponseBody;

import es.enxenio.smart.citydriver.model.events.measurement.MeasurementType;
import es.enxenio.smart.citydriver.model.events.measurement.service.MeasurementService;


@Controller
@RequestMapping("/events/measurement")
public class MeasurementController {

	@Autowired
	private MeasurementService measurementService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/listar")
	public void listar(
			ModelMap model, @RequestParam(value = "tipo", required = true) String tipo) {
		

	}
}
