(function() {
	'use strict';

	angular.module('app').factory('importShapefileService', importShapefileService);

	importShapefileService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function importShapefileService($http, $log, $q, $localStorage) {
		var service = {
				importar: importar
		};

		return service;
	
		function importar (importacion) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			var fd = new FormData();
			fd.append("file", importacion.filezip);
			
			return $http({
				method : 'POST',
				//withCredentials: true,
				transformRequest: angular.identity,
				headers: { 
			        'Content-Type': undefined 
			    },
				url : url_import_shape,
				data : importacion,
				params: {"lang": lang, "fdata": fd}
			});
		}
		
	}
})();