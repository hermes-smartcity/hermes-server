(function() {
	'use strict';

	angular.module('app').factory('userService', userService);

	userService.$inject = ['$http', '$log', '$q'];

	function userService($http, $log, $q) {
		var service = {
			getUser: getUser
		};

		return service;
	
		function getUser(url) {
		
			return $http({
				method : 'GET',
				url : url
			});
		}
	}
})();