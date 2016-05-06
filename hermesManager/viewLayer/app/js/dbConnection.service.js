(function() {
	'use strict';

	angular.module('app').factory('dbConnectionService', dbConnectionService);

	dbConnectionService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function dbConnectionService($http, $log, $q, $localStorage) {
		var service = {
				getDbConnectionsType: getDbConnectionsType,
				getDbConnections: getDbConnections,
				delet: delet,
				register: register,
				edit: edit,
				getDBConnection: getDBConnection
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
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_dbconnection + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (connection) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_dbconnection,
				data : connection,
				params: {"lang": lang}
			});
		}
		
		function edit (connection) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_dbconnection + "/" + connection.id,
				data : connection,
				params: {"lang": lang}
			});
		}
		
		function getDBConnection (id) {		
			return $http({
				method : 'GET',
				url : url_get_dbconnection,
				params: {"id": id}
			});
		}
	}
})();