(function() {
	'use strict';

	angular.module('app').factory('dataServicesService', dataServicesService);

	dataServicesService.$inject = ['$http', '$log', '$q'];

	function dataServicesService($http, $log, $q) {
		var service = {
			getServices: getServices,
			getMethods: getMethods,
			getPeticionesPorDia: getPeticionesPorDia
		};

		return service;
	
		function getServices() {
			return $http.get(url_services)
				.then(getServicesComplete)
				.catch(getServicesFailed);
			function getServicesComplete(response) {
				return response.data;
			}
			function getServicesFailed(error) {
				$log.error('XHR Failed for getServices.' + error.data);
			}
		}
	
		// Opcion uno, asi, sino con then and catch
		function getMethods (service) {		
			return $http({
				method : 'GET',
				url : url_operations,
				params: {"service": service}
			});
		}
		
		function getPeticionesPorDia(urlEv) {
			
			return $http({
				method : 'GET',
				url : urlEv
			});
		}
	}
})();