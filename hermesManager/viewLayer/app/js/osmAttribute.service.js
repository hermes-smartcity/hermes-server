(function() {
	'use strict';

	angular.module('app').factory('osmAttributeService', osmAttributeService);

	osmAttributeService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function osmAttributeService($http, $log, $q, $localStorage) {
		var service = {
				getOsmAttributes: getOsmAttributes,
				delet: delet,
				register: register,
				edit: edit,
				getOsmAttribute: getOsmAttribute
		};

		return service;
		
		
		function getOsmAttributes(idOsmConcept) {
			return $http({
				method : 'GET',
				url : url_osmAttributes,
				params: {"idOsmConcept": idOsmConcept}
			});
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_osmattribute + "/" + id,
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
				url : url_register_osmattribute,
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
				url : url_edit_osmattribute + "/" + attribute.id,
				data : attribute,
				params: {"lang": lang}
			});
		}
		
		function getOsmAttribute (id) {		
			return $http({
				method : 'GET',
				url : url_get_osmattribute,
				params: {"id": id}
			});
		}
	}
})();