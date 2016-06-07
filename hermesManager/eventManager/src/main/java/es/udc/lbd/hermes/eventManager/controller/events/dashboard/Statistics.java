package es.udc.lbd.hermes.eventManager.controller.events.dashboard;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.model.events.contextData.service.ContextDataService;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.events.heartRateData.service.HeartRateDataService;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.sleepData.service.SleepDataService;
import es.udc.lbd.hermes.model.events.stepsData.service.StepsDataService;
import es.udc.lbd.hermes.model.events.useractivities.service.UserActivitiesService;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.service.UserCaloriesExpendedService;
import es.udc.lbd.hermes.model.events.userdistances.service.UserDistancesService;
import es.udc.lbd.hermes.model.events.userheartrates.service.UserHeartRatesService;
import es.udc.lbd.hermes.model.events.userlocations.service.UserLocationsService;
import es.udc.lbd.hermes.model.events.usersleep.service.UserSleepService;
import es.udc.lbd.hermes.model.events.usersteps.service.UserStepsService;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@Component("statisticsComponent")
public class Statistics implements Serializable{

	private static final long serialVersionUID = -6945193694192799509L;

	@Autowired private UsuarioMovilService usuarioMovilService;

	@Autowired private UsuarioWebService usuarioWebService;

	@Autowired private VehicleLocationService vehicleLocationService;

	@Autowired private DataSectionService dataSectionService;

	@Autowired private MeasurementService measurementService;

	@Autowired private DriverFeaturesService driverFeaturesService;

	@Autowired private StepsDataService stepsDataService;

	@Autowired private SleepDataService sleepDataService;

	@Autowired private HeartRateDataService heartRateDataService;

	@Autowired private ContextDataService contextDataService;
	
	@Autowired private UserLocationsService userLocationsService;
	
	@Autowired private UserActivitiesService userActivitiesService;
	
	@Autowired private UserDistancesService userDistancesService;
	
	@Autowired private UserStepsService userStepsService;
	
	@Autowired private UserCaloriesExpendedService userCaloriesExpendedService;
	
	@Autowired private UserHeartRatesService userHeartRatesService;
	
	@Autowired private UserSleepService userSleepService;
	
	private long contarUsuariosMovil;
	private long contarUsuariosWeb;
	private long numberActiveUsers;
	private long totalVLocations;
	private long totalDataScts;
	private long totalMeasurements;
	private long totalDriversF;
	private long totalStepsData;
	private long totalSleepData;
	private long totalHeartRateData;
	private long totalContextData;
	private long totalUserLocations;
	private long totalUserActivities;
	private long totalUserDistances;
	private long totalUserSteps;
	private long totalUserCaloriesExpended;
	private long totalUserHeartRates;
	private long totalUserSleep;

	public Statistics() {}
	
	public void updateStatistics() {
		this.contarUsuariosMovil = usuarioMovilService.contar();
		this.contarUsuariosWeb = usuarioWebService.contar();
		this.numberActiveUsers = usuarioMovilService.getNumberActiveUsers();
		this.totalVLocations = vehicleLocationService.contar();
		this.totalDataScts = dataSectionService.contar();
		this.totalMeasurements = measurementService.contar();
		this.totalDriversF = driverFeaturesService.contar();
		this.totalStepsData = stepsDataService.contar();
		this.totalSleepData = sleepDataService.contar();
		this.totalHeartRateData = heartRateDataService.contar();
		this.totalContextData = contextDataService.contar();
		this.totalUserLocations = userLocationsService.contar();
		this.totalUserActivities = userActivitiesService.contar();
		this.totalUserDistances = userDistancesService.contar();
		this.totalUserSteps = userStepsService.contar();
		this.totalUserCaloriesExpended = userCaloriesExpendedService.contar();
		this.totalUserHeartRates = userHeartRatesService.contar();
		this.totalUserSleep = userSleepService.contar();
	}

	public long getContarUsuariosMovil() {
		return contarUsuariosMovil;
	}

	public void setContarUsuariosMovil(long contarUsuariosMovil) {
		this.contarUsuariosMovil = contarUsuariosMovil;
	}

	public long getContarUsuariosWeb() {
		return contarUsuariosWeb;
	}

	public void setContarUsuariosWeb(long contarUsuariosWeb) {
		this.contarUsuariosWeb = contarUsuariosWeb;
	}
	
	public long getNumberActiveUsers() {
		return numberActiveUsers;
	}

	public void setNumberActiveUsers(long numberActiveUsers) {
		this.numberActiveUsers = numberActiveUsers;
	}

	public long getTotalVLocations() {
		return totalVLocations;
	}

	public void setTotalVLocations(long totalVLocations) {
		this.totalVLocations = totalVLocations;
	}

	public long getTotalDataScts() {
		return totalDataScts;
	}

	public void setTotalDataScts(long totalDataScts) {
		this.totalDataScts = totalDataScts;
	}

	public long getTotalMeasurements() {
		return totalMeasurements;
	}

	public void setTotalMeasurements(long totalMeasurements) {
		this.totalMeasurements = totalMeasurements;
	}

	public long getTotalDriversF() {
		return totalDriversF;
	}

	public void setTotalDriversF(long totalDriversF) {
		this.totalDriversF = totalDriversF;
	}

	public long getTotalStepsData() {
		return totalStepsData;
	}

	public void setTotalStepsData(long totalStepsData) {
		this.totalStepsData = totalStepsData;
	}

	public long getTotalSleepData() {
		return totalSleepData;
	}

	public void setTotalSleepData(long totalSleepData) {
		this.totalSleepData = totalSleepData;
	}

	public long getTotalHeartRateData() {
		return totalHeartRateData;
	}

	public void setTotalHeartRateData(long totalHeartRateData) {
		this.totalHeartRateData = totalHeartRateData;
	}

	public long getTotalContextData() {
		return totalContextData;
	}

	public void setTotalContextData(long totalContextData) {
		this.totalContextData = totalContextData;
	}
	
	public long getTotalUserLocations() {
		return totalUserLocations;
	}

	public void setTotalUserLocations(long totalUserLocations) {
		this.totalUserLocations = totalUserLocations;
	}
	
	public long getTotalUserActivities() {
		return totalUserActivities;
	}

	public void setTotalUserActivities(long totalUserActivities) {
		this.totalUserActivities = totalUserActivities;
	}

	public long getTotalUserDistances() {
		return totalUserDistances;
	}

	public void setTotalUserDistances(long totalUserDistances) {
		this.totalUserDistances = totalUserDistances;
	}

	public long getTotalUserSteps() {
		return totalUserSteps;
	}

	public void setTotalUserSteps(long totalUserSteps) {
		this.totalUserSteps = totalUserSteps;
	}

	public long getTotalUserCaloriesExpended() {
		return totalUserCaloriesExpended;
	}

	public void setTotalUserCaloriesExpended(long totalUserCaloriesExpended) {
		this.totalUserCaloriesExpended = totalUserCaloriesExpended;
	}

	public long getTotalUserHeartRates() {
		return totalUserHeartRates;
	}

	public void setTotalUserHeartRates(long totalUserHeartRates) {
		this.totalUserHeartRates = totalUserHeartRates;
	}

	public long getTotalUserSleep() {
		return totalUserSleep;
	}

	public void setTotalUserSleep(long totalUserSleep) {
		this.totalUserSleep = totalUserSleep;
	}
	
	
}
