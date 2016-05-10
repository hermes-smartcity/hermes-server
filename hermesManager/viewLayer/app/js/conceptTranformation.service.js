(function() {
	'use strict';

	angular.module('app').factory('conceptTransformationService', conceptTransformationService);

	conceptTransformationService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function conceptTransformationService($http, $log, $q, $localStorage) {
		var service = {
				getConceptTransformations: getConceptTransformations,
				delet: delet,
				register: register,
				edit: edit,
				getConceptTransformation: getConceptTransformation,
				getDbConcepts: getDbConcepts,
				getOsmConcepts: getOsmConcepts
		};

		return service;
				
		function getConceptTransformations() {
			return $http.get(url_concepttransformations)
				.then(getConceptTransformationComplete)
				.catch(getConceptTransformationFailed);
			function getConceptTransformationComplete(response) {
				return response.data;
			}
			function getConceptTransformationFailed(error) {
				$log.error('XHR Failed for getConceptTransformation.' + error.data);
			}
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_concepttransformation + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (concepttransformation) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_concepttransformation,
				data : concepttransformation,
				params: {"lang": lang}
			});
		}
		
		function edit (concepttransformation) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_concepttransformation + "/" + concepttransformation.id,
				data : concepttransformation,
				params: {"lang": lang}
			});
		}
		
		function getConceptTransformation (id) {		
			return $http({
				method : 'GET',
				url : url_get_concepttransformation,
				params: {"id": id}
			});
		}
		
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
		
		function getOsmConcepts() {
			return $http.get(url_osmConcepts)
				.then(getOsmConceptsComplete)
				.catch(getOsmConceptsFailed);
			function getOsmConceptsComplete(response) {
				return response.data;
			}
			function getOsmConceptsFailed(error) {
				$log.error('XHR Failed for getOsmConcepts.' + error.data);
			}
		}
		
	}
})();