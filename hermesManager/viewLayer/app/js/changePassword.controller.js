(function() {
	'use strict';

	angular.module('app').controller('ChangePasswordController', ChangePasswordController);

	ChangePasswordController.$inject = ['userService', '$translate'];

	function ChangePasswordController(userService, $translate) {
		
		var vm = this;
		vm.change = change;
		
		function change() {
			var newPassword = {passwordOld: vm.passwordOld, passwordNew1: vm.passwordNew1, passwordNew2: vm.passwordNew2};		
			userService.changePassword(newPassword).then(changePasswordComplete);	
			
			function changePasswordComplete(response) {	
				//Cambiar el value por la internacionalizacion
				var key = response.data.key;
				var newValue = $translate.instant(key);
				response.data.value = newValue;
				
				vm.infoChange = response.data;
				
			}
			
		}
		

	}
})();