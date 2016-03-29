(function() {
	'use strict';

	angular.module('app').factory('logService', logService);

	logService.$inject = ['$http', '$log', '$q'];

	function logService($http, $log, $q) {
		var service = {
				deleteLog: deleteLog
		};

		return service;
	
		
		function deleteLog (id) {		
			return $http({
				method : 'DELETE',
				url : url_delete_log+"/"+id
			});
		}
	}
})();