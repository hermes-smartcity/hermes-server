(function() {
	'use strict';

	angular.module('app').controller('EventManagerController', EventManagerController);

	EventManagerController.$inject = [ '$scope', '$http', '$timeout', '$log',
			'$filter', 'eventsService' ];

	function EventManagerController($scope, $http, $timeout, $log, $filter,
			eventsService) {

		var vm = this;
		vm.aplicarFiltros = aplicarFiltros;

		eventsService.getStateEventManager().then(getStateEventManagerComplete);
		eventsService.getEventsToday().then(getEventsTodayComplete);
		eventsService.getEvensType().then(getEvensTypeComplete);
		eventsService.getUsuarios().then(getUsuariosComplete);
		eventsService.getMeasurementsType().then(getMeasurementsTypeComplete);
		eventsService.getEventoProcesado().then(getEventoProcesadoComplete);
		eventsService.getTotalVLocations().then(getTotalVLocationsComplete);
		eventsService.getTotalDataScts().then(getTotalDataSctsComplete);
		eventsService.getTotalMeasurements().then(getTotalMeasurementsComplete);
		eventsService.getTotalDriversF().then(getTotalDriversFComplete);
		/*
		 * TODO nose donde tiene que ir exactamente - es una prueba para saber
		 * como van los gráficos
		 */
		eventsService.getEventosPorDia().then(getEventosPorDiaComplete);

		function getStateEventManagerComplete(response) {
			$scope.active = response.data;
		}

		function getEventsTodayComplete(response) {
			$scope.eventsToday = response.data;
		}

		function getEvensTypeComplete(response) {
			$scope.eventsType = response.data;
		}

		function getUsuariosComplete(response) {
			$scope.usuarios = response.data;
		}

		function getMeasurementsTypeComplete(response) {
			$scope.measurementsType = response.data;
		}

		function getEventoProcesadoComplete(response) {
			$scope.eventoProcesado = response.data;
		}

		function getTotalVLocationsComplete(response) {
			$scope.totalL = response.data;
		}

		function getTotalDataSctsComplete(response) {
			$scope.totalDS = response.data;
		}

		function getTotalMeasurementsComplete(response) {
			$scope.totalM = response.data;
		}

		function getTotalDriversFComplete(response) {
			$scope.totalDF = response.data;
		}

		function getEventosPorDiaComplete(response) {
			$scope.eventosPorDia = response.data;

			/* * * * Prueba gráficos */
			$scope.labels = $scope.eventosPorDia.fechas;
			$scope.series = [ 'Días', 'Eventos'];
			$scope.data = [$scope.eventosPorDia.nEventos];

			/* * * * * */
	
			  $scope.onClick = function (points, evt) {
			    console.log(points, evt);
			  };
		}
		
		// Inicializamos el filtro de event type para que inicialmente liste
		// vehicle Locations
		$scope.eventTypeSelected = "VEHICLE_LOCATION";
		$scope.showButton = true; 
		$scope.startDate = new Date();
		// Inicializamos la fecha de inicio a la de ayer
		$scope.startDate.setDate($scope.startDate.getDate() - 1);
		$scope.endDate = new Date();
		
	


		function aplicarFiltros() {

			var pos = $scope.eventTypeSelected.indexOf('_');
			var value = $scope.eventTypeSelected.substr(0, pos);
			value += $scope.eventTypeSelected.substr(pos + 1,
					$scope.eventTypeSelected.length);
			value = angular.lowercase(value);

//			if (angular.equals($scope.eventTypeSelected, "VEHICLE_LOCATION"))
//				vm.pintarMapaVehicleLocations();
//			else if (angular.equals($scope.eventTypeSelected, "DATA_SECTION"))
//				vm.pintarMapaDataSections();
//			else if ($scope.measurementTypes.indexOf($scope.eventTypeSelected) > -1) {
//				vm.pintarMapaMeasurements();
//			} else
//				console.log("No corresponde a ningún tipo --> En construcción");

		}
		;



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
				url += "fechaFin=" + _endDate + "&";
			}
			return url;
		}

		// Preparar la url que va a llamar al controlador. TODO falta buscar una
		// manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
		function prepararUrl(esLng, esLat, wnLng, wnLat) {
			var url = "";
			if ($scope.usuarioSelected != null)
				url += "idUsuario=" + $scope.usuarioSelected.id + "&";
			url += prepararParametrosFechas();
			url += "wnLng=" + wnLng + "&wnLat=" + wnLat + "&esLng=" + esLng
					+ "&esLat=" + esLat;
			return url;
		};


		// Inicialmente sé que voy a pintar los vehicleLocation (la opción por
		// defecto en el select)
//		vm.aplicarFiltros();

	}
})();