(function() {
	'use strict';

	angular.module('app').factory('smartDriverService', smartDriverService);

	smartDriverService.$inject = ['$http', '$log', '$q'];

	function smartDriverService($http, $log, $q) {
		var service = {
				getServices: getServices,
				getMethods: getMethods,
				getTypes: getTypes,
				getDataSections: getDataSections,
				getLinkInformation: getLinkInformation,
				getAggregateMeasurement: getAggregateMeasurement,
				getComputeRoute: getComputeRoute
		};

		return service;
	
		function getServices() {
			return $http.get(url_smartdriver_services)
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
				url : url_smartdriver_methods,
				params: {"service": service}
			});
		}
		
		function getTypes() {
			return $http.get(url_types)
				.then(getTypesComplete)
				.catch(getTypesFailed);
			function getTypesComplete(response) {
				return response.data;
			}
			function getTypesFailed(error) {
				$log.error('XHR Failed for getTypes.' + error.data);
			}
		}
		
		function getDataSections() {
			return $http.get(url_datasections)
				.then(getDataSectionsComplete)
				.catch(getDataSectionsFailed);
			function getDataSectionsComplete(response) {
				return response.data;
			}
			function getDataSectionsFailed(error) {
				$log.error('XHR Failed for getDataSections.' + error.data);
			}
		}
		
		function getLinkInformation (currentLong, currentLat, previousLong, previousLat) {
			return $http({
				method : 'GET',
				url : url_network_link + "currentLat=" + currentLat + "&currentLong=" + currentLong + "&previousLat=" + previousLat + "&previousLong=" + previousLong
			});
		}
		
		function getAggregateMeasurement (type, lat, long, day, time, value) {
			return $http({
				method : 'GET',
				url : url_measurement_aggregate + "type=" + type + "&lat=" + lat + "&lon=" + long + "&day=" + day + "&time=" + time + "&value=" + value
			});
		}
		
		function getComputeRoute (fromLat, fromLng, toLat, toLng) {
			return $http({
				method : 'GET',
				url : url_network_route + "fromLat=" + fromLat + "&fromLng=" + fromLng + "&toLat=" + toLat + "&toLng=" + toLng
			});
		}
		

	}
})();