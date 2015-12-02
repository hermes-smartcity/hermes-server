(function() {
	'use strict';

	angular.module('app').factory('eventsService', eventsService);

	eventsService.$inject = ['$http', '$log', '$q'];

	function eventsService($http, $log, $q) {
		var service = {
			getStateEventManager: getStateEventManager,
			getEventsToday: getEventsToday,
			getEvensType: getEvensType,
			getUsuarios: getUsuarios,
			getMeasurementsType: getMeasurementsType,
			getEventoProcesado: getEventoProcesado,
			getTotalVLocations: getTotalVLocations,
			getTotalDataScts: getTotalDataScts,
			getTotalMeasurements: getTotalMeasurements,
			getTotalDriversF: getTotalDriversF,
			getEventosPorDia: getEventosPorDia
		};

		return service;
	
		function getStateEventManager() {
			var urlGet = "api/dashboard/json/stateEventManager";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getEventsToday() {
			var urlGetEventsToday = "api/dashboard/json/eventsToday";
			return $http({
				method : 'GET',
				url : urlGetEventsToday
			});
		}
		
		function getEvensType() {
			var urlGetEvensType = "api/dashboard/json/eventsType";
			return $http({
				method : 'GET',
				url : urlGetEvensType
			});
		}
		
		function getEvensType() {
			var urlGetEvensType = "api/dashboard/json/eventsType";
			return $http.get(urlGetEvensType)
				.then(getEvensTypeComplete)
				.catch(getEvensTypeFailed);
			function getEvensTypeComplete(response) {
				return response.data;
			}
			function getEvensTypeFailed(error) {
				$log.error('XHR Failed for getEvensType.' + error.data);
			}
		}
		
		function getUsuarios() {
			var urlGet = "api/dashboard/json/usuarios";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getMeasurementsType() {
			var urlGet = "api/dashboard/json/measurementTypes";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getEventoProcesado() {
			var urlGet = "api/dashboard/json/eventoProcesado";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getTotalVLocations() {
			var urlGet = "api/dashboard/json/totalVLocations";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getTotalDataScts() {
			var urlGet = "api/dashboard/json/totalDataScts";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getTotalMeasurements() {
			var urlGet = "api/dashboard/json/totalMeasurements";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getTotalDriversF() {
			var urlGet = "api/dashboard/json/totalDriversF";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getEventosPorDia() {
			var urlGet = "api/dashboard/json/eventosPorDia";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
	}
})();