package es.udc.lbd.hermes.model.dataservice.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.dataservice.DataServices;
import es.udc.lbd.hermes.model.dataservice.dao.DataServicesDao;
import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;

@Service("dataServiceService")
@Transactional
public class DataServicesServiceImpl implements DataServicesService{

	@Autowired
	private DataServicesDao dataServiceDao;
	
	@Override
	public void create(DataServices dataService) {
		dataServiceDao.create(dataService);
	}

	@Transactional(readOnly = true)
	public List<DataServices> obterDataService(){
		return dataServiceDao.obterDataService();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterPeticionesPorDia(String service, String method, Calendar fechaIni, Calendar fechaFin){
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = dataServiceDao.peticionesPorDia(service, method, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
}
