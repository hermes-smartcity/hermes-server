(function() {
	'use strict';

	angular.module('app').factory('sensorDataService', sensorDataService);

	sensorDataService.$inject = ['$http', '$log', '$q'];

	function sensorDataService($http, $log, $q) {
		var service = {

			getInfoSensoresPorDia: getInfoSensoresPorDia,
		};

		return service;
	
		function getInfoSensoresPorDia(urlEv) {
		
			return $http({
				method : 'GET',
				url : urlEv
			});
		}
		
	}
})();