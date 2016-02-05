(function() {
	'use strict';

	angular.module('app').controller('LoginController', LoginController);

	LoginController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'authenticateService', 'userService', '$stateParams'];

	function LoginController($rootScope, $scope, $http, $location, $state, authenticateService, userService, $stateParams) {
		
	var vm = this;
	vm.login = login;
	vm.activarCuenta = activarCuenta;
	
	function activarCuenta() {
		userService.getInfoCuenta($stateParams.email, $stateParams.hash).then(getInfoCuentaComplete);
	
		function getInfoCuentaComplete(response) {		
			vm.infoCuenta = response.data;		
		}
	}
	  
	
	function login() {
		
		authenticateService.authenticate($.param({username: vm.username, password: vm.password}),
				function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			
			
			if ($scope.rememberMe) {
				$cookieStore.put('authToken', authToken);
			}
			
			userService.getUser(url_get_user).then(getUserComplete);
			function getUserComplete(response) {
				$rootScope.user = response.data;
				$location.path("/");
				$state.go("dashboard");				
			}

		});
	
		
	}
	

	  
	 }
})();