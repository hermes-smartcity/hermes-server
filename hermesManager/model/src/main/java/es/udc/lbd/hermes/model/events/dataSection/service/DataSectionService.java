package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;

public interface DataSectionService {

	public DataSection get(Long id);
	
	public void create(DataSection dataSection, String sourceId);
	
	public void update(DataSection dataSection);
	
	public void delete(Long id);

	public List<DataSection> obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
//	public List<DataSection> obterDataSectionsByBounds(Double wnLng, Double wnLat, Double esLng, Double esLat);
	
//	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario);
}
