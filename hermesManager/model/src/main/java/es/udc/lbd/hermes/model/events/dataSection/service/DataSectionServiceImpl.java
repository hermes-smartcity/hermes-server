package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;


@Service("dataSectionService")
@Transactional
public class DataSectionServiceImpl implements DataSectionService {
	
	@Autowired
	private DataSectionDao dataSectionDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public DataSection get(Long id) {
		return dataSectionDao.get(id);
	}

	@Override
	public void create(DataSection dataSection) {			
		dataSectionDao.create(dataSection);		
	}

	@Override
	public void update(DataSection dataSection) {
		dataSectionDao.update(dataSection);
	}

	@Override
	public void delete(Long id) {
		DataSection dataSection = dataSectionDao.get(id);
		if (dataSection != null) {
			dataSectionDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<DataSection> obterDataSections() {
		List<DataSection> dataSections = dataSectionDao.obterDataSections();
		return dataSections;
	}
	
	@Transactional(readOnly = true)
	public List<DataSection> obterDataSectionsSegunUsuario(Long idUsuario) {
		List<DataSection> dataSections = dataSectionDao.obterDataSectionsSegunUsuario(idUsuario);
		return dataSections;
	}
}
