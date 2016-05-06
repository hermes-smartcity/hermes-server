(function() {
	'use strict';

	angular.module('app').factory('dbConceptService', dbConceptService);

	dbConceptService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function dbConceptService($http, $log, $q, $localStorage) {
		var service = {
				getDbConcepts: getDbConcepts,
				delet: delet,
				register: register,
				edit: edit,
				getDbConcept: getDbConcept,
				getDbConnections: getDbConnections
		};

		return service;
				
		function getDbConcepts() {
			return $http.get(url_dbConcepts)
				.then(getDbConceptsComplete)
				.catch(getDbConceptsFailed);
			function getDbConceptsComplete(response) {
				return response.data;
			}
			function getDbConceptsFailed(error) {
				$log.error('XHR Failed for getDbConcepts.' + error.data);
			}
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_dbconcept + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (concept) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_dbconcept,
				data : concept,
				params: {"lang": lang}
			});
		}
		
		function edit (concept) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_dbconcept + "/" + concept.id,
				data : concept,
				params: {"lang": lang}
			});
		}
		
		function getDbConcept (id) {		
			return $http({
				method : 'GET',
				url : url_get_dbconcept,
				params: {"id": id}
			});
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
	}
})();