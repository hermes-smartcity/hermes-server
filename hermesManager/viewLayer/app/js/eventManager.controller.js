(function() {
	'use strict';

	angular.module('app').controller('EventManagerController', EventManagerController);

	EventManagerController.$inject = ['$state', '$interval', '$scope', 'eventsType', 'usuarios' ,'measurementsType', 
	                                  '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope',
	                                  'totalMUsers', 'totalWebUsers', 'numberActiveUsers', 'eventsToday', 
	                                  'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                  'totalSTD', 'totalSLD', 'totalHRD', 'totalCD', '$translate'];

	function EventManagerController($state, $interval, $scope, eventsType, usuarios, measurementsType,  $http, $timeout, $log, $filter,
			eventsService, $rootScope, totalMUsers, totalWebUsers, numberActiveUsers, eventsToday, 
			eventoProcesado, totalL, totalDS, totalM, totalDF, totalSTD, totalSLD, totalHRD, totalCD,
			$translate) {
		var vm = this;
		vm.aplicarFiltros = aplicarFiltros;
		vm.eventsType = eventsType;
		vm.usuarios = usuarios;
		vm.measurementsType = measurementsType;
		vm.arrancar = arrancar;
		vm.parar = parar;
		vm.pintarGraficoVehicleLocations = pintarGraficoVehicleLocations;
		vm.pintarGraficoDataSections = pintarGraficoDataSections;
		vm.pintarGraficoMeasurements = pintarGraficoMeasurements;
		vm.recuperarYpintarEventos = recuperarYpintarEventos;
		vm.pintarGraficoContextData = pintarGraficoContextData;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.getLiveChartData = getLiveChartData;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		
		vm.totalMUsers = totalMUsers;
		vm.totalWebUsers = totalWebUsers;
		vm.numberActiveUsers = numberActiveUsers;
		vm.eventsToday = eventsToday;
		vm.eventoProcesado = eventoProcesado;
		vm.totalL = totalL;	
		vm.totalDS = totalDS;
		vm.totalM = totalM;
		vm.totalDF = totalDF;
		vm.totalSTD = totalSTD;
		vm.totalSLD = totalSLD;
		vm.totalHRD = totalHRD;
		vm.totalCD = totalCD;
		
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
		
		function aplicarFiltros() {
			var pos = vm.eventTypeSelected.indexOf('_');
			var value = vm.eventTypeSelected.substr(0, pos);
			value += vm.eventTypeSelected.substr(pos + 1,
					vm.eventTypeSelected.length);
			value = angular.lowercase(value);

			if (angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION"))
				vm.pintarGraficoVehicleLocations();
			else if (angular.equals(vm.eventTypeSelected, "DATA_SECTION"))
				vm.pintarGraficoDataSections();
			else if (angular.equals(vm.eventTypeSelected, "CONTEXT_DATA"))
				vm.pintarGraficoContextData();
			else if (vm.measurementsType.indexOf(vm.eventTypeSelected) > -1) {
				vm.pintarGraficoMeasurements();
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