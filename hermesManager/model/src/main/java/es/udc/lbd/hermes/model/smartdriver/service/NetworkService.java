package es.udc.lbd.hermes.model.smartdriver.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.util.exceptions.PointDestinyException;
import es.udc.lbd.hermes.model.util.exceptions.PointOriginException;

public interface NetworkService {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat);
	public AggregateMeasurementVO getAggregateMeasurement(Type type, Double lat, Double lon, Integer day, Integer time, String value);
	public List<RouteSegment> getComputeRoute(Double fromLat, Double fromLng, Double toLat, Double toLng) throws PointDestinyException, PointOriginException;
	
	
	public List<VehicleLocation> getVehicleLocation(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat);
	public List<Measurement> getMeasurement(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat);
	public List<DataSection> getDataSection(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat);
	public List<DriverFeatures> getDriverFeatures(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public List<HeartRateData> getHeartRateData(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public List<StepsData> getStepsData(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public List<SleepData> getSleepData(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public List<ContextData> getContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat);
	public List<UserLocations> getUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double nwLng, Double nwLat,	Double seLng, Double seLat);
	public List<UserActivities> getUserActivities(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}

