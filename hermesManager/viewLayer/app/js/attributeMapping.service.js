(function() {
	'use strict';

	angular.module('app').factory('attributeMappingService', attributeMappingService);

	attributeMappingService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function attributeMappingService($http, $log, $q, $localStorage) {
		var service = {
				getAttributeMappings: getAttributeMappings,
				delet: delet,
				register: register,
				edit: edit,
				getAttributeMapping: getAttributeMapping,
				getDbAttributes: getDbAttributes,
				getOsmAttributes: getOsmAttributes
		};

		return service;
		
		function getAttributeMappings(idConceptTransformation) {
			return $http({
				method : 'GET',
				url : url_attributemappings,
				params: {"idConceptTransformation": idConceptTransformation}
			});
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_attributemapping + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (attributemapping) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_attributemapping,
				data : attributemapping,
				params: {"lang": lang}
			});
		}
		
		function edit (attributemapping) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_attributemapping + "/" + attributemapping.id,
				data : attributemapping,
				params: {"lang": lang}
			});
		}
		
		function getAttributeMapping (id) {		
			return $http({
				method : 'GET',
				url : url_get_attributemapping,
				params: {"id": id}
			});
		}
		
		function getDbAttributes(idDbConcept) {
			return $http({
				method : 'GET',
				url : url_dbAttributes,
				params: {"idConcept": idDbConcept}
			});
		}
		
		function getOsmAttributes(idOsmConcept) {
			return $http({
				method : 'GET',
				url : url_osmAttributes,
				params: {"idOsmConcept": idOsmConcept}
			});
		}
		
	}
})();