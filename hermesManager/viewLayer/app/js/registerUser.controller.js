(function() {
	'use strict';

	angular.module('app').controller('RegisterUserController', RegisterUserController);

	RegisterUserController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'userService'];

	function RegisterUserController($rootScope, $scope, $http, $location, $state, userService) {
		
		var vm = this;
		vm.register = register;
		
		function register() {
			var usuarioNuevo = {email: vm.email, password: vm.password};		
			userService.registerUser(usuarioNuevo).then(registerUserComplete);	
			
			function registerUserComplete(response) {	
				vm.infoCuenta = response.data;
			}
			
		}
		

	}
})();