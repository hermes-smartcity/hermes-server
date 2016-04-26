(function() {
	'use strict';

	angular.module('app').controller('SensorDataController', SensorDataController);

	SensorDataController.$inject = ['$state', '$interval', '$scope', 'usuarios', 
	                                  '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope',
	                                  'eventsToday', 'eventoProcesado', 'statistics', '$translate',
	                                  'sensorDataService'];

	function SensorDataController($state, $interval, $scope, usuarios, $http, $timeout, $log, $filter,
			eventsService, $rootScope, eventsToday, eventoProcesado, statistics,
			$translate, sensorDataService) {
		var vm = this;
		vm.aplicarFiltros = aplicarFiltros;
		vm.sensorsType = $rootScope.sensorsType;
		vm.usuarios = usuarios;
		vm.measurementsType = $rootScope.measurementsType;
		vm.arrancar = arrancar;
		vm.parar = parar;
		
		vm.pintarGrafico = pintarGrafico;
		
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
		
		vm.recuperarYpintarSensores = recuperarYpintarSensores;
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		function recuperarYpintarSensores(urlGet){

			sensorDataService.getInfoSensoresPorDia(urlGet).then(getInfoSensoresPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getInfoSensoresPorDiaComplete(response) {

				vm.infoPorDia = response.data;
				vm.labels = vm.infoPorDia.labels;
				vm.series = vm.infoPorDia.series;
				vm.data = vm.infoPorDia.data;
				
				vm.onClick = function (points, evt) {
				    console.log(points, evt);
				};
				  
				// Si no hay eventos que cumplan los requisitos marcados en los filtros entonces se actualiza con el gráfico
				//getLiveChartData();
				  
				  
				
			}
			
		}
		
		// Inicializamos el filtro de sensor type para que inicialmente liste
		// TYPE_ACCELEROMETER
		vm.sensorTypeSelected = "TYPE_ACCELEROMETER";
		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la del mes anterior
		vm.startDate.setDate(vm.startDate.getDate() - 31);
		vm.endDate = new Date();
		
		  
		function getLiveChartData () {
		      if (vm.data.length === 0) {
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
		

		function pintarGrafico() {
			var url = url_infoPorDia;	
			url +="sensor="+vm.sensorTypeSelected+"&";
			url+=prepararUrl();
			vm.recuperarYpintarSensores(url);
		}
		
		function aplicarFiltros() {
			vm.pintarGrafico();
		}
		
		 function onTimeSetStart() {
			    vm.showCalendarStart = !vm.showCalendarStart;
		 }
		 
		 function onTimeSetEnd() {
			    vm.showCalendarEnd = !vm.showCalendarEnd;
		 }

		// Inicialmente sé que voy a pintar los type acceleremoter (la opción por
		// defecto en el select)
		vm.aplicarFiltros();

	}
})();