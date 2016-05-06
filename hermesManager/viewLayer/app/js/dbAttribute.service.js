(function() {
	'use strict';

	angular.module('app').factory('dbAttributeService', dbAttributeService);

	dbAttributeService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function dbAttributeService($http, $log, $q, $localStorage) {
		var service = {
				getDbAttributesType: getDbAttributesType,
				getDbAttributes: getDbAttributes,
				delet: delet,
				register: register,
				edit: edit,
				getDbAttribute: getDbAttribute
		};

		return service;
		
		function getDbAttributesType() {
			return $http.get(url_dbAttributesTypes)
				.then(getDbAttributesTypeComplete)
				.catch(getDbAttributesTypeFailed);
			function getDbAttributesTypeComplete(response) {
				return response.data;
			}
			function getDbAttributesTypeFailed(error) {
				$log.error('XHR Failed for getDbAttributesType.' + error.data);
			}
		}
		
		function getDbAttributes(idConcept) {
			return $http({
				method : 'GET',
				url : url_dbAttributes,
				params: {"idConcept": idConcept}
			});
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_dbattribute + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (attribute) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_dbattribute,
				data : attribute,
				params: {"lang": lang}
			});
		}
		
		function edit (attribute) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_dbattribute + "/" + attribute.id,
				data : attribute,
				params: {"lang": lang}
			});
		}
		
		function getDbAttribute (id) {		
			return $http({
				method : 'GET',
				url : url_get_dbattribute,
				params: {"id": id}
			});
		}
	}
})();