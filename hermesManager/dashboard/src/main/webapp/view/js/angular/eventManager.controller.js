(function() {
	'use strict';

	angular.module('app').controller('EventManagerController', EventManagerController);

	EventManagerController.$inject = ['$state', '$interval', '$scope', 'eventsType', 'usuarios' ,'measurementsType', 
	                                  '$http', '$timeout', '$log', '$filter', 'eventsService' ];

	function EventManagerController($state, $interval, $scope, eventsType, usuarios, measurementsType,  $http, $timeout, $log, $filter,
			eventsService) {
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
		
		eventsService.getStateActualizado().then(getStateActualizadoComplete);
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		function recuperarYpintarEventos(urlGet){

			eventsService.getEventosPorDia(urlGet).then(getEventosPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getEventosPorDiaComplete(response) {

				vm.eventosPorDia = response.data;
				vm.labels = vm.eventosPorDia.fechas;
				vm.series = [ 'Días', 'Eventos'];
				vm.data = [vm.eventosPorDia.nEventos];
					
				  vm.onClick = function (points, evt) {
				    console.log(points, evt);
				  };

				  // Si no hay eventos que cumplan los requisitos marcados en los filtros entonces se actualiza con el gráfico
				  getLiveChartData();

					function getLiveChartData () {
					      if (!vm.data[0].length) {
					    	vm.labels = [0];
					        vm.data[0] = [0];
					      }
					}
			}
			
		};
		
		// Inicializamos el filtro de event type para que inicialmente liste
		// vehicle Locations
		vm.eventTypeSelected = "VEHICLE_LOCATION";
		$scope.startDate = new Date();
		// Inicializamos la fecha de inicio a la de ayer
		$scope.startDate.setDate($scope.startDate.getDate() - 31);
		$scope.endDate = new Date();
		
		function arrancar() {
			eventsService.arrancar();
			$state.go('inicio');
		}
		
		function parar() {
			eventsService.parar();
			$state.go('inicio');
		}



		// Preparar las fechas para pasarlas como parametro a los controladores
		function prepararParametrosFechas() {
			var url = "";
			if ($scope.startDate != null) {
				var _startDate = $filter('date')($scope.startDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaIni=" + _startDate + "&";
			}
			if ($scope.endDate != null) {
				var _endDate = $filter('date')($scope.endDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaFin=" + _endDate;
			}
			return url;
		}

		// Preparar la url que va a llamar al controlador. TODO falta buscar una
		// manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
		function prepararUrl() {
			var url = "";
			if (vm.usuarioSelected != null)
				url += "idUsuario=" + vm.usuarioSelected.id + "&";
			url += prepararParametrosFechas();
			
			return url;
		};
		
		function pintarGraficoVehicleLocations() {
			var urlGet = "api/vehiclelocation/json/eventosPorDia?";
			urlGet+=prepararUrl();
			vm.recuperarYpintarEventos(urlGet);
		}
		
		function pintarGraficoDataSections() {
			var urlGet = "api/datasection/json/eventosPorDia?";
			urlGet+=prepararUrl();
			vm.recuperarYpintarEventos(urlGet);
		}
		
		function pintarGraficoMeasurements() {
			var urlGet = "api/measurement/json/eventosPorDia?tipo="+vm.eventTypeSelected+"&";
			urlGet+=prepararUrl();
			vm.recuperarYpintarEventos(urlGet);
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
			else if (vm.measurementsType.indexOf(vm.eventTypeSelected) > -1) {
				vm.pintarGraficoMeasurements();
			} else
				console.log("No corresponde a ningún tipo --> En construcción");

		}
		;

		// Inicialmente sé que voy a pintar los vehicleLocation (la opción por
		// defecto en el select)
		vm.aplicarFiltros();

	}
})();