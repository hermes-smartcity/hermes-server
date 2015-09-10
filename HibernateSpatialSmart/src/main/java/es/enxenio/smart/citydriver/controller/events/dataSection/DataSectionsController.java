package es.enxenio.smart.citydriver.controller.events.dataSection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.dataSection.service.DataSectionService;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.service.VehicleLocationService;
import es.enxenio.smart.citydriver.web.rest.events.MainResource;


@RestController
@RequestMapping(value = "/events/dataSection")
public class DataSectionsController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(DataSectionsController.class);

	@Autowired
	private DataSectionService dataSectionServicio;

	@RequestMapping(value="/json/dataSectionsByUsuario", method = RequestMethod.GET)
	public List<DataSection> getDataSections(@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		return dataSectionServicio.obterDataSectionsSegunUsuario(idUsuario);

	}
	
	@RequestMapping(value="/json/dataSections", method = RequestMethod.GET)
	public List<DataSection> getDataSections() {
		return dataSectionServicio.obterDataSections();

	}

}
