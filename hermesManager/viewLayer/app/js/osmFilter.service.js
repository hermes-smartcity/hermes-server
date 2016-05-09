(function() {
	'use strict';

	angular.module('app').factory('osmFilterService', osmFilterService);

	osmFilterService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function osmFilterService($http, $log, $q, $localStorage) {
		var service = {
				getOsmFiltersOperation: getOsmFiltersOperation,
				getOsmFilters: getOsmFilters,
				delet: delet,
				register: register,
				edit: edit,
				getOsmFilter: getOsmFilter
		};

		return service;
		
		function getOsmFiltersOperation() {
			return $http.get(url_osmFiltersOperation)
				.then(getOsmFiltersOperationComplete)
				.catch(getOsmFiltersOperationFailed);
			function getOsmFiltersOperationComplete(response) {
				return response.data;
			}
			function getOsmFiltersOperationFailed(error) {
				$log.error('XHR Failed for getOsmFiltersOperation.' + error.data);
			}
		}
		
		function getOsmFilters(idOsmConcept) {
			return $http({
				method : 'GET',
				url : url_osmFilters,
				params: {"idOsmConcept": idOsmConcept}
			});
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_osmfilter + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (filter) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_osmfilter,
				data : filter,
				params: {"lang": lang}
			});
		}
		
		function edit (filter) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_osmfilter + "/" + filter.id,
				data : filter,
				params: {"lang": lang}
			});
		}
		
		function getOsmFilter (id) {		
			return $http({
				method : 'GET',
				url : url_get_osmfilter,
				params: {"id": id}
			});
		}
	}
})();