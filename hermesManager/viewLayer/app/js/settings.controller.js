(function() {
	'use strict';

	angular.module('app').controller('SettingsController', SettingsController);

	SettingsController.$inject = ['datosSettings', 'settingsService', '$translate'];

	function SettingsController(datosSettings, settingsService, $translate) {
		
		var vm = this;
		vm.datosSettings = datosSettings;
		vm.update = update;
		
		function update() {
					
			settingsService.updateSettings(vm.datosSettings).then(updateSettingsComplete);	
			
			function updateSettingsComplete(response) {	
				//Cambiar el value por la internacionalizacion
				var key = response.data.key;
				var newValue = $translate.instant(key);
				response.data.value = newValue;
				
				vm.infoUpdate = response.data;
			}
			
		}
	}
})();