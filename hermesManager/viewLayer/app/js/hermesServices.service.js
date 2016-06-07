(function() {
	'use strict';

	angular.module('app').factory('hermesServicesService', hermesServicesService);

	hermesServicesService.$inject = ['$http', '$log', '$q', '$filter', '$localStorage'];

	function hermesServicesService($http, $log, $q, $filter, $localStorage) {
		var service = {
				getServices: getServices,
				getMethods: getMethods,
				getTypes: getTypes,
				getMeasurementTypes: getMeasurementTypes,
				getDataSections: getDataSections,
				getLinkInformation: getLinkInformation,
				getAggregateMeasurement: getAggregateMeasurement,
				getComputeRoute: getComputeRoute,
				getVehicleLocation: getVehicleLocation,
				getMeasurement: getMeasurement,
				getDataSection: getDataSection,
				getDriverFeatures: getDriverFeatures,
				getHeartRateData: getHeartRateData,
				getSleepData: getSleepData,
				getStepsData: getStepsData,
				getContextData: getContextData,
				getUserLocations: getUserLocations,
				getUserActivities: getUserActivities,
				getUserDistances: getUserDistances,
				getUserSteps: getUserSteps,
				getUserCaloriesExpended: getUserCaloriesExpended,
				getUserHeartRates: getUserHeartRates,
				getUserSleep: getUserSleep
		};

		return service;
	
		function getServices() {
			return $http.get(url_hermesS_services)
				.then(getServicesComplete)
				.catch(getServicesFailed);
			function getServicesComplete(response) {
				return response.data;
			}
			function getServicesFailed(error) {
				$log.error('XHR Failed for getServices.' + error.data);
			}
		}
	
		// Opcion uno, asi, sino con then and catch
		function getMethods (service) {		
			return $http({
				method : 'GET',
				url : url_hermesS_methods,
				params: {"service": service}
			});
		}
		
		function getTypes() {
			return $http.get(url_hermesS_types)
				.then(getTypesComplete)
				.catch(getTypesFailed);
			function getTypesComplete(response) {
				return response.data;
			}
			function getTypesFailed(error) {
				$log.error('XHR Failed for getTypes.' + error.data);
			}
		}
		
		function getMeasurementTypes() {
			return $http.get(url_hermesS_measurementTypes)
				.then(getMeasurementTypesComplete)
				.catch(getMeasurementTypesFailed);
			function getMeasurementTypesComplete(response) {
				return response.data;
			}
			function getMeasurementTypesFailed(error) {
				$log.error('XHR Failed for getMeasurementTypes.' + error.data);
			}
		}
		
		function getDataSections() {
			return $http.get(url_datasections)
				.then(getDataSectionsComplete)
				.catch(getDataSectionsFailed);
			function getDataSectionsComplete(response) {
				return response.data;
			}
			function getDataSectionsFailed(error) {
				$log.error('XHR Failed for getDataSections.' + error.data);
			}
		}
		
		function getLinkInformation (currentLong, currentLat, previousLong, previousLat) {
			return $http({
				method : 'GET',
				url : url_network_link + "currentLat=" + currentLat + "&currentLong=" + currentLong + "&previousLat=" + previousLat + "&previousLong=" + previousLong
			});
		}
		
		function getAggregateMeasurement (type, lat, long, day, time, value) {
			return $http({
				method : 'GET',
				url : url_measurement_aggregate + "type=" + type + "&lat=" + lat + "&lon=" + long + "&day=" + day + "&time=" + time + "&value=" + value
			});
		}
	
	
		function getComputeRoute (fromLat, fromLng, toLat, toLng) {
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'GET',
				url : url_network_route + "fromLat=" + fromLat + "&fromLng=" + fromLng + "&toLat=" + toLat + "&toLng=" + toLng,
				params: {"lang": lang}
			});
			
		}
		
		
		function prepararParametrosFechas(startDate, endDate){
			var url = "";
			if(startDate!==null){
				var _startDate = $filter('date')(startDate, 'yyyy-MM-dd HH:mm:ss');
				url += "from="+ _startDate+"&";
			}
			if(endDate!==null){
				var _endDate = $filter('date')(endDate, 'yyyy-MM-dd HH:mm:ss');
				url += "to="+ _endDate+"&";
			}
			return url;
		}

		function prepararUrl(startDate, endDate, seLng, seLat, nwLng, nwLat){		
			var url ="";
			
			url+=prepararParametrosFechas(startDate, endDate);		
			url+="seLng="+seLng+"&seLat="+seLat+"&nwLng="+nwLng+"&nwLat="+nwLat;	
			return url;
		}
		
		
		
		function getVehicleLocation (from, to, seLng, seLat, nwLng, nwLat) {
			
			var url = url_get_vehicle_locations;
			url+=prepararUrl(from, to, seLng, seLat, nwLng, nwLat);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getVehicleLocation.' + error.data);
			}
			
		}
		
		function getMeasurement (tipo, from, to, seLng, seLat, nwLng, nwLat) {
			
			var url = url_get_measurements;
			url+=prepararUrl(from, to, seLng, seLat, nwLng, nwLat);
			url+="&tipo=" + tipo;
			
			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getMeasurement.' + error.data);
			}
			
		}
		
		function getDataSection (from, to, seLng, seLat, nwLng, nwLat) {
			
			var url = url_get_data_sections;
			url+=prepararUrl(from, to, seLng, seLat, nwLng, nwLat);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getDataSection.' + error.data);
			}
			
		}
		
		function getDriverFeatures (from, to) {
			
			var url = url_get_driver_features;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getDriverFeatures.' + error.data);
			}
		}
		
		function getHeartRateData (from, to) {
			
			var url = url_get_heart_rate_data;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getHeartRateData.' + error.data);
			}
		}
		
		function getStepsData (from, to) {
			
			var url = url_get_steps_data;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getStepsData.' + error.data);
			}
		}
		
		function getSleepData (from, to) {
			
			var url = url_get_sleep_data;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getSleepData.' + error.data);
			}
		}
		
		function getContextData (from, to, seLng, seLat, nwLng, nwLat) {
			
			var url = url_get_context_data;
			url+=prepararUrl(from, to, seLng, seLat, nwLng, nwLat);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getContextData.' + error.data);
			}
		}
		
		function getUserLocations (from, to, seLng, seLat, nwLng, nwLat) {
		
			var url = url_get_user_locations;
			url+=prepararUrl(from, to, seLng, seLat, nwLng, nwLat);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserLocations.' + error.data);
			}
		}
		
		function getUserActivities (from, to) {
			
			var url = url_get_user_activities;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserActivities.' + error.data);
			}
		}
		
		function getUserDistances (from, to) {
			
			var url = url_get_user_distances;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserDistances.' + error.data);
			}
		}
		
		function getUserSteps (from, to) {
			
			var url = url_get_user_steps;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserSteps.' + error.data);
			}
		}
		
		function getUserCaloriesExpended (from, to) {
			
			var url = url_get_user_calories_expended;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserCaloriesExpended.' + error.data);
			}
		}
		
		function getUserHeartRates (from, to) {
			
			var url = url_get_user_heart_rates;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserHeartRates.' + error.data);
			}
		}
		
		function getUserSleep (from, to) {
			
			var url = url_get_user_sleep;
			url+=prepararParametrosFechas(from, to);

			return $http.get(url)
				.then(getRequestComplete)
				.catch(getRequestFailed);
			
			function getRequestComplete(response) {
				return response.data;
			}
			
			function getRequestFailed(error) {
				$log.error('XHR Failed for getUserSleep.' + error.data);
			}
		}

	}
})();