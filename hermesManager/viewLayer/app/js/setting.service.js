(function() {
	'use strict';

	angular.module('app').factory('settingsService', settingsService);

	settingsService.$inject = ['$http', '$log', '$q'];

	function settingsService($http, $log, $q) {
		var service = {
			getSettings: getSettings,
			updateSettings: updateSettings,
			getSetting: getSetting,
		};

		return service;
	
		function getSettings() {
			return $http.get(url_settings)
				.then(getSettingsComplete)
				.catch(getSettingsFailed);
			function getSettingsComplete(response) {
				return response.data;
			}
			function getSettingsFailed(error) {
				$log.error('XHR Failed for getSettings.' + error.data);
			}
		}
		
		function updateSettings (settings) {		
			return $http({
				method : 'POST',
				url : url_update_settings,
				data : settings
			});
		}
		
		function getSetting (name) {		
			return $http({
				method : 'GET',
				url : url_get_setting,
				params: {"name": name}
			});
		}
	
	}
})();