(function() {
	'use strict';

	angular.module('app').factory('messageService', messageService);

	messageService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function messageService($http, $log, $q, $localStorage) {
		var service = {
				getMessages: getMessages,
		};

		return service;
		
		function getMessages(idExecution) {
			return $http({
				method : 'GET',
				url : url_messages,
				params: {"idExecution": idExecution}
			});
		}
		
	}
})();