(function() {
	'use strict';

	angular.module('app').controller('EventManagerController', EventManagerController);

	EventManagerController.$inject = ['$state', '$interval', '$scope', 'usuarios', 
	                                  '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope',
	                                  'eventsToday', 'eventoProcesado', 'statistics', '$translate'];

	function EventManagerController($state, $interval, $scope, usuarios, $http, $timeout, $log, $filter,
			eventsService, $rootScope, eventsToday, eventoProcesado, statistics,
			$translate) {
		var vm = this;
		vm.aplicarFiltros = aplicarFiltros;
		vm.eventsType = $rootScope.eventsType;
		vm.usuarios = usuarios;
		vm.measurementsType = $rootScope.measurementsType;
		vm.arrancar = arrancar;
		vm.parar = parar;
		vm.pintarGraficoVehicleLocations = pintarGraficoVehicleLocations;
		vm.pintarGraficoDataSections = pintarGraficoDataSections;
		vm.pintarGraficoMeasurements = pintarGraficoMeasurements;
		vm.pintarGraficoContextData = pintarGraficoContextData;
		vm.pintarGraficoUserLocations = pintarGraficoUserLocations;
		vm.pintarGraficoUserActivities = pintarGraficoUserActivities;
		vm.pintarGraficoDriverFeatures = pintarGraficoDriverFeatures;
		vm.pintarGraficoStepsData = pintarGraficoStepsData;
		vm.pintarGraficoSleepData = pintarGraficoSleepData;
		vm.pintarGraficoHeartRateData = pintarGraficoHeartRateData;
		vm.pintarGraficoUserDistances = pintarGraficoUserDistances;
		vm.pintarGraficoUserSteps = pintarGraficoUserSteps;
		vm.pintarGraficoUserCaloriesExpended = pintarGraficoUserCaloriesExpended;
		vm.pintarGraficoUserHeartRates = pintarGraficoUserHeartRates;
		vm.pintarGraficoUserSleep = pintarGraficoUserSleep;
		vm.recuperarYpintarEventos = recuperarYpintarEventos;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.getLiveChartData = getLiveChartData;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		
		vm.eventsToday = eventsToday;
		vm.eventoProcesado = eventoProcesado;
		
		vm.totalMUsers = statistics.contarUsuariosMovil;
		vm.totalWebUsers = statistics.contarUsuariosWeb;
		vm.numberActiveUsers = statistics.numberActiveUsers;
		vm.totalL = statistics.totalVLocations;	
		vm.totalDS = statistics.totalDataScts;
		vm.totalM = statistics.totalMeasurements;
		vm.totalDF = statistics.totalDriversF;
		vm.totalSTD = statistics.totalStepsData;
		vm.totalSLD = statistics.totalSleepData;
		vm.totalHRD = statistics.totalHeartRateData;
		vm.totalCD = statistics.totalContextData;
		vm.totalUL = statistics.totalUserLocations;
		vm.totalUA = statistics.totalUserActivities;
		vm.totalUDI = statistics.totalUserDistances;
		vm.totalUST = statistics.totalUserSteps;
		vm.totalUCE = statistics.totalUserCaloriesExpended;
		vm.totalUHR = statistics.totalUserHeartRates;
		vm.totalUSL = statistics.totalUserSleep;
		
		vm.totalGoogleFit = vm.totalUL + vm.totalUA + vm.totalUDI + vm.totalUST + vm.totalUCE + vm.totalUHR + vm.totalUSL;
		vm.totalFitBit = vm.totalSTD + vm.totalSLD + vm.totalHRD;
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		function recuperarYpintarEventos(urlGet){

			eventsService.getEventosPorDia(urlGet).then(getEventosPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getEventosPorDiaComplete(response) {

				vm.eventosPorDia = response.data;
				vm.labels = vm.eventosPorDia.fechas;
				vm.series = [ $translate.instant('numeroEventos')];
				vm.data = [vm.eventosPorDia.nEventos];
		
				vm.onClick = function (points, evt) {
				    console.log(points, evt);
				};
				  
				// Si no hay eventos que cumplan los requisitos marcados en los filtros entonces se actualiza con el gráfico
				getLiveChartData();
				  
			}
			
		}
		
		// Inicializamos el filtro de event type para que inicialmente liste
		// vehicle Locations
		vm.eventTypeSelected = "VEHICLE_LOCATION";
		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la del mes anterior
		vm.startDate.setDate(vm.startDate.getDate() - 31);
		vm.endDate = new Date();
		
		  
		function getLiveChartData () {
		      if (!vm.data[0].length) {
		    	vm.labels = [0];
		        vm.data[0] = [0];
		      }
		}
		
		function arrancar() {
			var resultado = {
					value : "Running",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.arrancar();
		}
		
		function parar() {
			var resultado = {
					value : "Stopped",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.parar();
		}

		// Preparar las fechas para pasarlas como parametro a los controladores
		function prepararParametrosFechas() {
			var url = "";
			if (vm.startDate !== null) {
				var _startDate = $filter('date')(vm.startDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaIni=" + _startDate + "&";
			}
			if (vm.endDate !== null) {
				var _endDate = $filter('date')(vm.endDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaFin=" + _endDate;
			}
			return url;
		}

		// Preparar la url que va a llamar al controlador. TODO falta buscar una
		// manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
		function prepararUrl() {
			var url = "";
			if (typeof vm.usuarioSelected != 'undefined' && vm.usuarioSelected !== null)
				url += "idUsuario=" + vm.usuarioSelected.id + "&";
			url += prepararParametrosFechas();
			
			return url;
		}
		
		function pintarGraficoVehicleLocations() {
			var url = url_eventosPorDiaVL;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
				
		function pintarGraficoDataSections() {
			var url = url_eventosPorDiaDS;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoMeasurements() {
			var url = url_eventosPorDiaM;
			url +="tipo="+vm.eventTypeSelected+"&";
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoContextData() {
			var url = url_eventosPorDiaCD;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserLocations() {
			var url = url_eventosPorDiaUL;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserActivities() {
			var url = url_eventosPorDiaUA;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoDriverFeatures() {
			var url = url_eventosPorDiaDF;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoSleepData() {
			var url = url_eventosPorDiaSLD;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoStepsData() {
			var url = url_eventosPorDiaSTD;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoHeartRateData() {
			var url = url_eventosPorDiaHRD;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserDistances() {
			var url = url_eventosPorDiaUDI;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserSteps() {
			var url = url_eventosPorDiaUST;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserCaloriesExpended() {
			var url = url_eventosPorDiaUCE;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserHeartRates() {
			var url = url_eventosPorDiaUHR;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function pintarGraficoUserSleep() {
			var url = url_eventosPorDiaUSL;	
			url+=prepararUrl();
			vm.recuperarYpintarEventos(url);
		}
		
		function aplicarFiltros() {
			var pos = vm.eventTypeSelected.indexOf('_');
			var value = vm.eventTypeSelected.substr(0, pos);
			value += vm.eventTypeSelected.substr(pos + 1,
					vm.eventTypeSelected.length);
			value = angular.lowercase(value);

			if (angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
				vm.pintarGraficoVehicleLocations();
			}else if (angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
				vm.pintarGraficoDataSections();
			}else if (angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
				vm.pintarGraficoContextData();
			}else if (angular.equals(vm.eventTypeSelected, "USER_LOCATIONS")){
				vm.pintarGraficoUserLocations();
			}else if (angular.equals(vm.eventTypeSelected, "USER_ACTIVITIES")){
				vm.pintarGraficoUserActivities();
			}else if (angular.equals(vm.eventTypeSelected, "DRIVER_FEATURES")){
				vm.pintarGraficoDriverFeatures();
			}else if (angular.equals(vm.eventTypeSelected, "STEPS_DATA")){
				vm.pintarGraficoStepsData();
			}else if (angular.equals(vm.eventTypeSelected, "SLEEP_DATA")){
				vm.pintarGraficoSleepData();
			}else if (angular.equals(vm.eventTypeSelected, "HEART_RATE_DATA")){
				vm.pintarGraficoHeartRateData();
			}else if (vm.measurementsType.indexOf(vm.eventTypeSelected) > -1) {
				vm.pintarGraficoMeasurements();
			}else if (angular.equals(vm.eventTypeSelected, "USER_DISTANCES")){
				vm.pintarGraficoUserDistances();
			}else if (angular.equals(vm.eventTypeSelected, "USER_STEPS")){
				vm.pintarGraficoUserSteps();
			}else if (angular.equals(vm.eventTypeSelected, "USER_CALORIES_EXPENDED")){
				vm.pintarGraficoUserCaloriesExpended();
			}else if (angular.equals(vm.eventTypeSelected, "USER_HEART_RATES")){
				vm.pintarGraficoUserHeartRates();
			}else if (angular.equals(vm.eventTypeSelected, "USER_SLEEP")){
				vm.pintarGraficoUserSleep();
			} else
				console.log("No corresponde a ningún tipo --> En construcción");

		}
		
		 function onTimeSetStart() {
			    vm.showCalendarStart = !vm.showCalendarStart;
		 }
		 
		 function onTimeSetEnd() {
			    vm.showCalendarEnd = !vm.showCalendarEnd;
		 }

		// Inicialmente sé que voy a pintar los vehicleLocation (la opción por
		// defecto en el select)
		vm.aplicarFiltros();

	}
})();