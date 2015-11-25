(function() {
	'use strict';

	angular.module('app').factory('eventsService', eventsService);

	eventsService.$inject = ['$http', '$log', '$q'];

	function eventsService($http, $log, $q) {
		var service = {
			getEvents: getEvents
		};

		return service;

//		function getEvents() {
//			var ret = [];
//			ret.$promise = $q.defer();
//			var urlGet = "../vehiclelocation/json/vehicleLocations";
//			$http.get(urlGet)
//				.then(getEventsComplete)
//				.catch(getEventsFailed);
//
//			function getEventsComplete(response) {
//				angular.copy(response.data, ret);
//				console.log("complete -- "+ret.length);
//				ret.$promise.resolve(ret);
//			}
//			function getEventsFailed(error) {
//				$log.error('XHR Failed for getEvents.' + error.data);
//			}
//			
//			console.log("devuelvo -- "+ret.length);
//			return ret;
//		}
		
		function getEvents() {
			var urlGet = "../vehiclelocation/json/vehicleLocations";
			return $http.get(urlGet)
				.then(getEventsComplete)
				.catch(getEventsFailed);
			function getEventsComplete(response) {
				return response.data;
			}
			function getEventsFailed(error) {
				$log.error('XHR Failed for getEvents.' + error.data);
			}
		}


	}
})();