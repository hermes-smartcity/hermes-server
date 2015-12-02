package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.dao.BloqueElementos;


@Service("dataSectionService")
@Transactional
public class DataSectionServiceImpl implements DataSectionService {
	
	private static final int ELEMENTOS_PAXINA = 100;
	
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
		List<DataSection> dataSections = dataSectionDao.obterDataSections(idUsuario, fechaIni, fechaFin, polygon, -1, -1);
		return dataSections;
	}
	
	@Transactional(readOnly = true)
	public BloqueElementos<DataSection> obterDataSectionsPaginados(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat, int paxina) {

		/*
		 * Obten count+1 dataSections para determinar si existen mais
		 * dataSections no rango especificado.
		 */
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<DataSection> dataSections = dataSectionDao.obterDataSections(idUsuario, fechaIni, 
				fechaFin, polygon, ELEMENTOS_PAXINA * (paxina - 1), ELEMENTOS_PAXINA + 1);

		boolean haiMais = dataSections.size() == (ELEMENTOS_PAXINA + 1);

		/*
		 * Borra o ultimo dataSection da lista devolta si existen mais
		 * dataSections no rango especificado
		 */
		if (haiMais) {
			dataSections.remove(dataSections.size() - 1);
		}

		long numero = dataSectionDao.contar();
		/* Return BloqueElementos. */
		return new BloqueElementos<DataSection>(dataSections, numero,
				ELEMENTOS_PAXINA, paxina, haiMais);

	}
	
	@Transactional(readOnly = true)
	public long contar(){
		return dataSectionDao.contar();
	}
	
	@Transactional(readOnly = true)
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<Long> listaN = new ArrayList<Long>();
		List<EventosPorDia> ed = dataSectionDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNumeroEventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
}
