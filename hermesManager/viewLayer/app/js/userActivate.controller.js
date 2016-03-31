(function() {
	'use strict';

	angular.module('app').controller('ActivateUserController', ActivateUserController);

	ActivateUserController.$inject = ['$http','$state', 'userService', '$stateParams'];

	function ActivateUserController($http, $state, userService, $stateParams) {

		var vm = this;
		vm.activarCuenta = activarCuenta;

		function activarCuenta() {
			userService.getInfoCuenta($stateParams.email, $stateParams.hash).then(getInfoCuentaComplete);

			function getInfoCuentaComplete(response) {		
				vm.infoCuenta = response.data;		
			}
		}
	}
})();