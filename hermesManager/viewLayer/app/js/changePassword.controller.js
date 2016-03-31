(function() {
	'use strict';

	angular.module('app').controller('ChangePasswordController', ChangePasswordController);

	ChangePasswordController.$inject = ['userService'];

	function ChangePasswordController(userService) {
		
		var vm = this;
		vm.change = change;
		
		function change() {
			var newPassword = {passwordOld: vm.passwordOld, passwordNew1: vm.passwordNew1, passwordNew2: vm.passwordNew2};		
			userService.changePassword(newPassword).then(changePasswordComplete);	
			
			function changePasswordComplete(response) {	
				vm.infoChange = response.data;
			}
			
		}
		

	}
})();