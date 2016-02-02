(function() {
	'use strict';

	angular.module('app').factory('userRestService', userRestService);

	userRestService.$inject = ['$resource'];

	function userRestService($resource) {
		return $resource(url_user + '/:id', 
                {id: '@entityId'},
            {
		        'update': { method:'PUT' }
		    });
	}
})();