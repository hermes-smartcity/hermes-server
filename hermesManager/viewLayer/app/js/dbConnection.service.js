(function() {
	'use strict';

	angular.module('app').factory('dbConnectionService', dbConnectionService);

	dbConnectionService.$inject = ['$http', '$log', '$q'];

	function dbConnectionService($http, $log, $q) {
		var service = {
				getDbConnectionsType: getDbConnectionsType,
				getDbConnections: getDbConnections,
				delet: delet,
		};

		return service;
		
		function getDbConnectionsType() {
			return $http.get(url_dbConnectionsTypes)
				.then(getDbConnectionsTypeComplete)
				.catch(getDbConnectionsTypeFailed);
			function getDbConnectionsTypeComplete(response) {
				return response.data;
			}
			function getDbConnectionsTypeFailed(error) {
				$log.error('XHR Failed for getDbConnectionsType.' + error.data);
			}
		}
		
		function getDbConnections() {
			return $http.get(url_dbConnections)
				.then(getDbConnectionsComplete)
				.catch(getDbConnectionsFailed);
			function getDbConnectionsComplete(response) {
				return response.data;
			}
			function getDbConnectionsFailed(error) {
				$log.error('XHR Failed for getDbConnections.' + error.data);
			}
		}
		
		function delet (id) {		
			return $http({
				method : 'DELETE',
				url : url_delete_dbconnection+"/"+id
			});
		}
	}
})();