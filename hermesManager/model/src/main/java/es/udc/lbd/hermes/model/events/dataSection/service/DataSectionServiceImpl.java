package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.util.HelpersModel;


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
	public void create(DataSection dataSection, String sourceId) {	
		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}		
		dataSection.setUsuario(usuario);
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
	public List<DataSection> obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<DataSection> dataSections = dataSectionDao.obterDataSections(idUsuario, fechaIni, fechaFin, polygon );
		return dataSections;
	}
}
