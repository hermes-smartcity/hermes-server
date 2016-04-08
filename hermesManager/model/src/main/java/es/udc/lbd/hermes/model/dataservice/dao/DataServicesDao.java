package es.udc.lbd.hermes.model.dataservice.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.dataservice.DataServices;
import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DataServicesDao extends GenericDao<DataServices, Long>{

	public List<DataServices> obterDataService();
	public List<EventosPorDia> peticionesPorDia(String service, String methos, Calendar fechaIni, Calendar fechaFin);
	
}
