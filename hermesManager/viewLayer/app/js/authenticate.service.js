(function() {
	'use strict';

	angular.module('app').factory('authenticateService', authenticateService);

	authenticateService.$inject = ['$resource'];

	function authenticateService($resource) {
		return $resource(url_authenticate, {},
				{
					authenticate: {
						method: 'POST',
						headers : {'Content-Type': 'application/x-www-form-urlencoded'}
						
					},
				}
			);
	}
})();