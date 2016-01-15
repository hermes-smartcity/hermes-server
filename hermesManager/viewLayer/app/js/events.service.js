(function() {
	'use strict';

	angular.module('app').factory('eventsService', eventsService);

	eventsService.$inject = ['$http', '$log', '$q'];

	function eventsService($http, $log, $q) {
		var service = {
//			getStateEventManager: getStateEventManager,
			getStateActualizado: getStateActualizado,
			arrancar: arrancar,
			parar:parar,
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
	
		
		function arrancar() {		
			var urlGet = url_servidor+"api/eventManager/arrancar";
			return $http.post(urlGet)
				.success(arrancarSuccess)
				.error(arrancarFailed);

			function arrancarSuccess() {
				$log.info('Arrancar success');
			}

			function arrancarFailed(error) {
				$log.error('XHR Failed for arrancar.' + error);
			}
		}
		
		function parar() {		
			var urlGet = url_servidor+"api/eventManager/parar";
			return $http.post(urlGet)
				.success(pararSuccess)
				.error(pararFailed);

			function pararSuccess() {
				$log.info('Parar success');
			}

			function pararFailed(error) {
				$log.error('XHR Failed for parar.' + error);
			}
		}
		
		function getStateActualizado() {
			var urlGet = url_servidor+"api/eventManager/json/stateEventManager";
			//var urlGet = "api/eventManager/json/stateEventManager";
			return $http({
				method : 'GET',
				url : urlGet
			});
		}
		
		function getEventsToday() {
			var urlGet = url_servidor+"api/dashboard/json/eventsToday";
			return $http.get(urlGet)
				.then(getEventsTodayComplete)
				.catch(getEventsTodayFailed);
			function getEventsTodayComplete(response) {
				return response.data;
			}
			function getEventsTodayFailed(error) {
				$log.error('XHR Failed for getEventsToday.' + error.data);
			}
		}
		
		function getEvensType() {
			var urlGet = url_servidor+"api/dashboard/json/eventsType";
			return $http.get(urlGet)
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
			var urlGet = url_servidor+"api/dashboard/json/usuarios";
			return $http.get(urlGet)
				.then(getUsuariosComplete)
				.catch(getUsuariosFailed);
			function getUsuariosComplete(response) {
				return response.data;
			}
			function getUsuariosFailed(error) {
				$log.error('XHR Failed for getUsuarios.' + error.data);
			}
		}
		
		function getMeasurementsType() {
			var urlGet = url_servidor+"api/dashboard/json/measurementTypes";
			return $http.get(urlGet)
				.then(getMeasurementsTypeComplete)
				.catch(getMeasurementsTypeFailed);
			function getMeasurementsTypeComplete(response) {
				return response.data;
			}
			function getMeasurementsTypeFailed(error) {
				$log.error('XHR Failed for getMeasurementsType.' + error.data);
			}
		}
		
		function getEventoProcesado() {
			var urlGet = url_servidor+"api/dashboard/json/eventoProcesado";
			return $http.get(urlGet)
				.then(getEventoProcesadoComplete)
				.catch(getEventoProcesadoFailed);
			function getEventoProcesadoComplete(response) {
				return response.data;
			}
			function getEventoProcesadoFailed(error) {
				$log.error('XHR Failed for getEventoProcesado.' + error.data);
			}
		}
		
		function getTotalVLocations() {
			var urlGet = url_servidor+"api/dashboard/json/totalVLocations";
			return $http.get(urlGet)
				.then(getTotalVLocationsComplete)
				.catch(getTotalVLocationsFailed);
			function getTotalVLocationsComplete(response) {
				return response.data;
			}
			function getTotalVLocationsFailed(error) {
				$log.error('XHR Failed for getTotalVLocations.' + error.data);
			}
		}
		
		function getTotalDataScts() {
			var urlGet = url_servidor+"api/dashboard/json/totalDataScts";
			return $http.get(urlGet)
				.then(getTotalDataSctsComplete)
				.catch(getTotalDataSctsFailed);
			function getTotalDataSctsComplete(response) {
				return response.data;
			}
			function getTotalDataSctsFailed(error) {
				$log.error('XHR Failed for getTotalDataScts.' + error.data);
			}
		}
	
		function getTotalMeasurements() {
			var urlGet = url_servidor+"api/dashboard/json/totalMeasurements";
			return $http.get(urlGet)
				.then(getUsuariosComplete)
				.catch(getUsuariosFailed);
			function getUsuariosComplete(response) {
				return response.data;
			}
			function getUsuariosFailed(error) {
				$log.error('XHR Failed for getTotalMeasurements.' + error.data);
			}
		}
		
		function getTotalDriversF() {
			var urlGet = url_servidor+"api/dashboard/json/totalDriversF";
			return $http.get(urlGet)
				.then(getTotalDriversFComplete)
				.catch(getTotalDriversFFailed);
			function getTotalDriversFComplete(response) {
				return response.data;
			}
			function getTotalDriversFFailed(error) {
				$log.error('XHR Failed for getTotalDriversF.' + error.data);
			}
		}
		
		function getEventosPorDia(urlEv) {
		
			return $http({
				method : 'GET',
				url : urlEv
			});
		}
	}
})();