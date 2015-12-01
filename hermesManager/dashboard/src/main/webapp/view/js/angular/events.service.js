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
			getTotalDriversF: getTotalDriversF
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
			var urlGetEventoProcesado = "api/dashboard/json/totalDriversF";
			return $http({
				method : 'GET',
				url : urlGetEventoProcesado
			});
		}
	}
})();