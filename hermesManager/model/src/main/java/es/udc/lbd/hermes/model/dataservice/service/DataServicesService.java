package es.udc.lbd.hermes.model.dataservice.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.dataservice.DataServices;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;

public interface DataServicesService {

	public void create(DataServices dataService);
	public List<DataServices> obterDataService();
	public ListaEventosYdias obterPeticionesPorDia(String service, String method, Calendar fechaIni, Calendar fechaFin);
}
