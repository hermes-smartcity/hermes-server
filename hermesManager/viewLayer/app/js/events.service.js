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
			return $http.post(url_arrancar)
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
			return $http.post(url_parar)
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
			return $http({
				method : 'GET',
				url : url_state
			});
		}
		
		function getEventsToday() {
			return $http.get(url_eventsToday)
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
			return $http.get(url_eventsType)
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
			return $http.get(url_usuarios)
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
			return $http.get(url_measurementTypes)
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
			return $http.get(url_eventoProcesado)
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
			return $http.get(url_totalVLocations)
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
			return $http.get(url_totalDataScts)
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
			return $http.get(url_totalMeasurements)
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
			return $http.get(url_totalDriversF)
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