package es.udc.lbd.hermes.model.smartdriver.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.dataservice.dao.DataServicesDao;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.dao.ContextDataDao;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.dao.DriverFeaturesDao;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.dao.HeartRateDataDao;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.dao.MeasurementDao;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.dao.SleepDataDao;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.dao.StepsDataDao;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.useractivities.dao.UserActivitiesDao;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.dao.UserCaloriesExpendedDao;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;
import es.udc.lbd.hermes.model.events.userdistances.dao.UserDistancesDao;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.dao.UserHeartRatesDao;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.userlocations.dao.UserLocationsDao;
import es.udc.lbd.hermes.model.events.usersleep.UserSleep;
import es.udc.lbd.hermes.model.events.usersleep.dao.UserSleepDao;
import es.udc.lbd.hermes.model.events.usersteps.UserSteps;
import es.udc.lbd.hermes.model.events.usersteps.dao.UserStepsDao;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.smartdriver.dao.NetworkDao;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.RegistroPeticionesHelper;
import es.udc.lbd.hermes.model.util.exceptions.PointDestinyException;
import es.udc.lbd.hermes.model.util.exceptions.PointOriginException;
import es.udc.lbd.hermes.model.util.exceptions.RouteException;


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
	
	@Autowired
	private DriverFeaturesDao driverFeaturesDao;
	
	@Autowired
	private HeartRateDataDao heartRateDataDao;
	
	@Autowired
	private StepsDataDao stepsDataDao;
	
	@Autowired
	private SleepDataDao sleepDataDao;
	
	@Autowired
	private ContextDataDao contextDataDao;
	
	@Autowired
	private UserLocationsDao userLocationsDao;
	
	@Autowired
	private UserActivitiesDao userActivitiesDao;
	
	@Autowired
	private UserDistancesDao userDistancesDao;
	
	@Autowired
	private UserStepsDao userStepsDao;
	
	@Autowired
	private UserCaloriesExpendedDao userCaloriesExpendedDao;
	
	@Autowired
	private UserHeartRatesDao userHeartRatesDao;
	
	@Autowired
	private UserSleepDao userSleepDao;
		
	@Override
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat){
		//Recuperamos el datos
		NetworkLinkVO resultado = networkDao.getLinkInformation(currentLong, currentLat, previousLong, previousLat);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.linkInformationSmartDriver();
		
		return resultado;
	}
	
	@Override
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
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
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<RouteSegment> getComputeRoute(Double fromLat, Double fromLng, Double toLat, Double toLng) throws PointDestinyException, PointOriginException, RouteException{
		//Obtenemos el id de origen
		Integer originPoint = networkDao.obtainOriginPoint(fromLat, fromLng);
		if (originPoint == null)
			throw new PointOriginException(fromLat, fromLng);
		
		//Obtenemos el id de destino
		Integer destinyPoint = networkDao.obtainDestinyPoint(toLat, toLng);
		if (destinyPoint == null)
			throw new PointDestinyException(toLat, toLng);
				
		//Obtenemos la lista de tramos
		try{
			List<RouteSegment> listado = networkDao.obtainListSections(originPoint, destinyPoint);
			
			//Registramos peticion realizada al servicio rest 
			RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
			registro.computeRouteSmartDriver();
					
			return listado;
			
		}catch(RouteException e){
			throw e;
		}
		
		
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<VehicleLocation> getVehicleLocation(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat) {
		
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);
		
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations(idUsuario, fechaIni, 
				fechaFin, polygon, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.vehicleLocationSmartDriver();
				
		return vehicleLocations;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<Measurement> getMeasurement(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);

		List<Measurement> measurements = measurementDao.obterMeasurementsSegunTipo(tipo, idUsuario, fechaIni, fechaFin, polygon, -1, -1);

		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.measurementSmartDriver();
				
		return measurements;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DataSection> getDataSection(Long idUsuarioMovil, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);

		List<DataSection> dataSections = dataSectionDao.obterDataSections(idUsuarioMovil, fechaIni, fechaFin, polygon, -1, -1);

		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.dataSectionsSmartDriver();
				
		return dataSections;
		
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DriverFeatures> getDriverFeatures(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		List<DriverFeatures> driverFeatures = driverFeaturesDao.obterDriverFeatures(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.driverFeaturesSmartDriver();
				
		return driverFeatures;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<HeartRateData> getHeartRateData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		List<HeartRateData> heartRateData = heartRateDataDao.obterHeartRateData(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.heartRateDataSmartCitizien();
				
		return heartRateData;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<StepsData> getStepsData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		List<StepsData> stepsData = stepsDataDao.obterStepsData(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.stepsDataSmartCitizien();
				
		return stepsData;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<SleepData> getSleepData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		List<SleepData> sleepData = sleepDataDao.obterSleepData(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.sleepDataSmartCitizien();
		
		return sleepData;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<ContextData> getContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat){
		
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);

		List<ContextData> contextData = contextDataDao.obterContextData(idUsuario, fechaIni, fechaFin, polygon, -1, -1);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.contextDataSmartCitizien();
				
		return contextData;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<UserLocations> getUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat){
		
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);

		List<UserLocations> userLocations = userLocationsDao.obterUserLocations(idUsuario, fechaIni, fechaFin, polygon, -1, -1);
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userLocationsSmartCitizien();
				
		return userLocations;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<UserActivities> getUserActivities(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserActivities> userActivities = userActivitiesDao.obterUserActivities(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userActivitiesSmartCitizien();
				
		return userActivities;
	}
	
	public List<UserDistances> getUserDistances(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserDistances> userDistances = userDistancesDao.obterUserDistances(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userDistancesSmartCitizien();
				
		return userDistances;
	}
	
	public List<UserSteps> getUserSteps(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserSteps> userSteps = userStepsDao.obterUserSteps(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userStepsSmartCitizien();
				
		return userSteps;
	}
	
	public List<UserCaloriesExpended> getUserCaloriesExpended(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserCaloriesExpended> userCaloriesExpended = userCaloriesExpendedDao.obterUserCaloriesExpended(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userCaloriesExpendedSmartCitizien();
				
		return userCaloriesExpended;
	}
	
	public List<UserHeartRates> getUserHeartRates(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserHeartRates> userHeartRates = userHeartRatesDao.obterUserHeartRates(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userHeartRatesSmartCitizien();
				
		return userHeartRates;
	}
	
	public List<UserSleep> getUserSleep(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		List<UserSleep> userSleep = userSleepDao.obterUserSleep(idUsuario, fechaIni, 
				fechaFin, -1, -1);	
		
		//Registramos peticion realizada al servicio rest 
		RegistroPeticionesHelper registro = new RegistroPeticionesHelper(dataServiceDao);
		registro.userSleepSmartCitizien();
				
		return userSleep;
	}
	
}
