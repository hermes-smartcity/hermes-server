(function() {
	'use strict';

	angular.module('app').factory('executionService', executionService);

	executionService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function executionService($http, $log, $q, $localStorage) {
		var service = {
				getExecutions: getExecutions,
				delet: delet
		};

		return service;
		
		function getExecutions() {
			return $http.get(url_executions)
				.then(getExecutionsComplete)
				.catch(getExecutionsFailed);
			function getExecutionsComplete(response) {
				return response.data;
			}
			function getExecutionsFailed(error) {
				$log.error('XHR Failed for getExecutions.' + error.data);
			}
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_execution + "/" + id,
				params: {"lang": lang}
			});
		}
		
	}
})();