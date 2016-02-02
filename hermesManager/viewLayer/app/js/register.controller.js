(function() {
	'use strict';

	angular.module('app').controller('RegisterController', RegisterController);

	RegisterController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'roles', 'userRestService'];

	function RegisterController($rootScope, $scope, $http, $location, $state, roles, userRestService) {
		
	var vm = this;
	vm.register = register;
	vm.roles = roles;
	vm.rolSelected = "ROLE_CONSULTA";
	
	function register() {
		var usuarioNuevo = {email: vm.email, password: vm.password, rol: vm.rolSelected};		
		userRestService.save(usuarioNuevo);		
		$state.go("userManager");
	}
	

	  
	 }
})();