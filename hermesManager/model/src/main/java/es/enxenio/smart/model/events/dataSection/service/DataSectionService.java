package es.enxenio.smart.model.events.dataSection.service;

import java.util.List;

import es.enxenio.smart.model.events.dataSection.DataSection;


public interface DataSectionService {

	public DataSection get(Long id);
	
	public void create(DataSection dataSection);
	
	public void update(DataSection dataSection);
	
	public void delete(Long id);

	public List<DataSection> obterDataSections();
	
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario);
}