(function() {
	'use strict';

	angular.module('app').factory('dashboardService', dashboardService);

	dashboardService.$inject = ['$http', '$log', '$q', '$filter'];

	function dashboardService($http, $log, $q, $filter) {

		var service = {
				recuperarDatosPeticion: recuperarDatosPeticion,
				recuperarDatosPeticionSinGeometria: recuperarDatosPeticionSinGeometria,
				recuperaGeojsonDdConcept: recuperaGeojsonDdConcept 
		};

		return service;

		function prepararParametrosFechas(startDate, endDate){
			var url = "";
			if(startDate!==null){
				var _startDate = $filter('date')(startDate, 'yyyy-MM-dd HH:mm:ss');
				url += "fechaIni="+ _startDate+"&";
			}
			if(endDate!==null){
				var _endDate = $filter('date')(endDate, 'yyyy-MM-dd HH:mm:ss');
				url += "fechaFin="+ _endDate+"&";
			}
			return url;
		}

		function prepararUrl(esLng, esLat, wnLng, wnLat, startDate, endDate, usuarioSelected){		
			var url ="";
			if (typeof usuarioSelected != 'undefined' && usuarioSelected !== null)
				url+="idUsuario="+ usuarioSelected.id+"&";		
			url+=prepararParametrosFechas(startDate, endDate);		
			url+="wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;	
			return url;
		}
		
		function prepararUrlSinGeometria(startDate, endDate, usuarioSelected){		
			var url ="";
			if (typeof usuarioSelected != 'undefined' && usuarioSelected !== null)
				url+="idUsuario="+ usuarioSelected.id+"&";		
			url+=prepararParametrosFechas(startDate, endDate);		
				
			return url;
		}

		function recuperarDatosPeticion(urlRequest, esLng, esLat, wnLng, wnLat, startDate, endDate, usuarioSelected) {

			var url = urlRequest;
			url+=prepararUrl(esLng, esLat, wnLng, wnLat, startDate, endDate, usuarioSelected);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for recuperarDatosPeticion.' + error.data);
			}
		}
		
		function recuperarDatosPeticionSinGeometria(urlRequest, startDate, endDate, usuarioSelected) {

			var url = urlRequest;
			url+=prepararUrlSinGeometria(startDate, endDate, usuarioSelected);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for recuperarDatosPeticionSinGeometria.' + error.data);
			}
		}
		
		function recuperaGeojsonDdConcept(dbconceptId, esLng, esLat, wnLng, wnLat) {

			var url = url_dbconcept_geojson;
			url+="?dbconceptId=" + dbconceptId + "&wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for recuperaGeojsonDdConcept.' + error.data);
			}
		}

	}

})();	