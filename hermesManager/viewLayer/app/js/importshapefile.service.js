(function() {
	'use strict';

	angular.module('app').factory('importShapefileService', importShapefileService);

	importShapefileService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function importShapefileService($http, $log, $q, $localStorage) {
		var service = {
				importar: importar,
				getCharsets: getCharsets,
		};

		return service;
	
		function getCharsets() {
			return $http.get(url_charsets)
				.then(getCharsetsComplete)
				.catch(getCharsetsFailed);
			function getCharsetsComplete(response) {
				return response.data;
			}
			function getCharsetsFailed(error) {
				$log.error('XHR Failed for getCharsets.' + error.data);
			}
		}
		
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
	                //formData.append("model", angular.toJson(data.model));
	                formData.append("dbConnection",data.model.dbConnection);
	                if ( data.model.dbConcept !== undefined){
	                	formData.append("dbConcept", data.model.dbConcept);	
	                }
	                if (data.model.dbConceptName !== undefined){
	                	formData.append("dbConceptName", data.model.dbConceptName);	
	                }else{
	                	formData.append("dbConceptName", undefined);
	                }
	                if (data.model.dbConceptSchema !== undefined){
	                	formData.append("dbConceptSchema", data.model.dbConceptSchema);	
	                }else{
	                	formData.append("dbConceptSchema", undefined);	
	                }               
	                if (data.model.keepExistingData === undefined){
	                	formData.append("keepExistingData", false);
	                }else{
	                	formData.append("keepExistingData", data.model.keepExistingData);	
	                }
	                formData.append("charset",data.model.charset);
	                formData.append("file", data.file);
	                return formData;
	            },
				data : {model: importacion, file: filezip},
				params: {"lang": lang}
			});
		}
		
	}
})();