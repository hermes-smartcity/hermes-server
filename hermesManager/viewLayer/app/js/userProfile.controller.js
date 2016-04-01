(function() {
	'use strict';

	angular.module('app').controller('UserProfileController', UserProfileController);

	UserProfileController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'datosUsuario', 'userService'];

	function UserProfileController($rootScope, $scope, $http, $location, $state, datosUsuario, userService) {

		var vm = this;
		vm.datosUsuario = datosUsuario;

	}
	
})();