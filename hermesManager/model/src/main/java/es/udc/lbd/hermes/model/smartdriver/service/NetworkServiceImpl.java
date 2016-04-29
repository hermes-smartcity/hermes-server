package es.udc.lbd.hermes.model.smartdriver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.dataservice.dao.DataServicesDao;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.events.measurement.dao.MeasurementDao;
import es.udc.lbd.hermes.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.smartdriver.dao.NetworkDao;
import es.udc.lbd.hermes.model.util.RegistroPeticionesHelper;
import es.udc.lbd.hermes.model.util.exceptions.PointDestinyException;
import es.udc.lbd.hermes.model.util.exceptions.PointOriginException;


@Service("networkService")
@Transactional
public class NetworkServiceImpl implements NetworkService{

	@Autowired
	private NetworkDao networkDao;
	
	@Autowired
	private VehicleLocationDao vehicleLocationDao;
	
	@Autowired
	private MeasurementDao measurementDao;
	
	@Autowired
	private DataSectionDao dataSectionDao;
	
	@Autowired
	private DataServicesDao dataServiceDao;
		
	@Override
	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat){
		//Recuperamos el datos
		NetworkLinkVO resultado = networkDao.getLinkInformation(currentLong, currentLat, previousLong, previousLat);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.linkInformationSmartDriver();
		
		return resultado;
	}
	
	@Override
	public AggregateMeasurementVO getAggregateMeasurement(Type type, Double lat, Double lon, Integer day, Integer time, String value){
		
		AggregateMeasurementVO resultado = null;
		//En funcion del tipo haremos la consulta en vehiclelocations o en measurement
		switch (type) {
		case VEHICLE_LOCATION:
			resultado = vehicleLocationDao.getAggregateValue(lat, lon, day, time);
			break;
			
		case HIGH_SPEED:
			resultado = measurementDao.getAggregateValue(type, lat, lon, day, time);
			break;
			
		case HIGH_ACCELERATION:
			resultado = measurementDao.getAggregateValue(type, lat, lon, day, time);
			break;
			
		case HIGH_DECELERATION:
			resultado = measurementDao.getAggregateValue(type, lat, lon, day, time);
			break;
			
		case HIGH_HEART_RATE:
			resultado = measurementDao.getAggregateValue(type, lat, lon, day, time);
			break;
			
		case DATA_SECTION:
			
			if (value != null){
				resultado = dataSectionDao.getAggregateValue(value, lat, lon, day, time);
			}else{
				resultado = new AggregateMeasurementVO();
			}
			
			break;
		
		default:
			break;
		}
	
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.aggregateMeasurementSmartDriver();
		
		return resultado;
	}
	
	public List<RouteSegment> getComputeRoute(Double fromLat, Double fromLng, Double toLat, Double toLng) throws PointDestinyException, PointOriginException{
		//Obtenemos el id de origen
		Integer originPoint = networkDao.obtainOriginPoint(fromLat, fromLng);
		if (originPoint == null)
			throw new PointOriginException(fromLat, fromLng);
		
		//Obtenemos el id de destino
		Integer destinyPoint = networkDao.obtainDestinyPoint(toLat, toLng);
		if (destinyPoint == null)
			throw new PointDestinyException(toLat, toLng);
				
		//Obtenemos la lista de tramos
		List<RouteSegment> listado = networkDao.obtainListSections(originPoint, destinyPoint);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.computeRouteSmartDriver();
				
		return listado;
	}
}
