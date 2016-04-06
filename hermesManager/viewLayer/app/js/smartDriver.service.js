(function() {
	'use strict';

	angular.module('app').factory('smartDriverService', smartDriverService);

	smartDriverService.$inject = ['$http', '$log', '$q'];

	function smartDriverService($http, $log, $q) {
		var service = {
				getMethods: getMethods,
				getLinkInformation: getLinkInformation
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
		
		function getLinkInformation (currentLong, currentLat, previousLong, previousLat) {		
			return $http({
				method : 'GET',
				url : url_network_link + "c=" + currentLat + "," + currentLong + "&p=" + previousLat + "," + previousLong
			});
		}

	}
})();