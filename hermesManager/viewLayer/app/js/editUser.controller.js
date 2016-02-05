(function() {
	'use strict';

	angular.module('app').controller('EditUserController', EditUserController);

	EditUserController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'usuariosMoviles', 
	                              'userService', '$stateParams'];

	function EditUserController($rootScope, $scope, $http, $location, $state, usuariosMoviles,
			userService, $stateParams) {
		
	var vm = this;
	vm.edit = edit;
	vm.usuariosMoviles = usuariosMoviles;

	userService.getUserToModify($stateParams.idUser).then(getUserToModifyComplete);
	
	function getUserToModifyComplete(response) {		
		vm.usuario = response.data;		
	}
	function edit() {
		var nuevoSourceIdMovil = "";
		if(typeof vm.usuarioSelected != "undefined")
			nuevoSourceIdMovil = vm.usuarioSelected.sourceId;
		var usuarioNuevo = {id:$stateParams.idUser, email: vm.email, password: vm.password, sourceIdUsuarioMovilNuevo: nuevoSourceIdMovil};	
		
		userService.editUser(usuarioNuevo).then(getEditUserComplete);
		
		function getEditUserComplete(response) {
			vm.infoCuenta = response.data;
//			$state.go("userManager");
		}
		
	}
	

	  
	 }
})();