(function() {
	'use strict';

	angular.module('app').controller('RegisterAdminController', RegisterAdminController);

	RegisterAdminController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'userService'];

	function RegisterAdminController($rootScope, $scope, $http, $location, $state, userService) {
		
	var vm = this;
	vm.register = register;
	
	function register() {
		var usuarioNuevo = {email: vm.email, password: vm.password};		
		userService.registerAdmin(usuarioNuevo).then(registerAdminComplete);	
		
		function registerAdminComplete(response) {	
			vm.infoCuenta = response.data;
			$state.go("userManager");
		}
		
	}

	 }
})();