(function() {
	'use strict';

	angular.module('app').factory('smartDriverService', smartDriverService);

	smartDriverService.$inject = ['$http', '$log', '$q'];

	function smartDriverService($http, $log, $q) {
		var service = {
				getMethods: getMethods,
				getTypes: getTypes,
				getDataSections: getDataSections,
				getLinkInformation: getLinkInformation,
				getAggregateMeasurement: getAggregateMeasurement
		};

		return service;
	
		function getMethods() {
			return $http.get(url_methods)
				.then(getMethodsComplete)
				.catch(getMethodsFailed);
			function getMethodsComplete(response) {
				return response.data;
			}
			function getMethodsFailed(error) {
				$log.error('XHR Failed for getMethods.' + error.data);
			}
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

	}
})();