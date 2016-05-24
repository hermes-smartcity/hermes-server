(function() {
	'use strict';

	angular.module('app').factory('importShapefileService', importShapefileService);

	importShapefileService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function importShapefileService($http, $log, $q, $localStorage) {
		var service = {
				importar: importar
		};

		return service;
	
		function importar (importacion, filezip) {
			
			var lang = $localStorage.hermesmanager.lang;
		   
			return $http({
				method : 'POST',
				url : url_import_shape,
				headers: { 
			        'Content-Type': undefined 
			    },
			    transformRequest: function (data) {
	                var formData = new FormData();
	                //need to convert our json object to a string version of json otherwise
	                // the browser will do a 'toString()' on the object which will result 
	                // in the value '[Object object]' on the server.
	                formData.append("model", angular.toJson(data.model));
	                formData.append("file", data.file);
	                return formData;
	            },
				data : {model: importacion, file: filezip},
				params: {"lang": lang}
			});
		}
		
	}
})();