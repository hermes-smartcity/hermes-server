(function() {
	'use strict';

	angular.module('app').factory('osmConceptService', osmConceptService);

	osmConceptService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function osmConceptService($http, $log, $q, $localStorage) {
		var service = {
				getOsmConcepts: getOsmConcepts,
				delet: delet,
				register: register,
				edit: edit,
				getOsmConcept: getOsmConcept
		};

		return service;
				
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
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_osmconcept + "/" + id,
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
				url : url_register_osmconcept,
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
				url : url_edit_osmconcept + "/" + concept.id,
				data : concept,
				params: {"lang": lang}
			});
		}
		
		function getOsmConcept (id) {		
			return $http({
				method : 'GET',
				url : url_get_osmconcept,
				params: {"id": id}
			});
		}
		
	}
})();