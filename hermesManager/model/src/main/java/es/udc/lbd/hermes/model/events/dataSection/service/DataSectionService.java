package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;

public interface DataSectionService {

	public DataSection get(Long id);
	
	public void create(DataSection dataSection, String sourceId);
	
	public void update(DataSection dataSection);
	
	public void delete(Long id);

	public List<DataSection> obterDataSections();
	
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario);
}
