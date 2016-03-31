(function() {
	'use strict';

	angular.module('app').controller('LoginController', LoginController);

	LoginController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'authenticateService', 'userService', '$localStorage'];

	function LoginController($rootScope, $scope, $http, $location, $state, authenticateService, userService, $localStorage) {
		
	var vm = this;
	vm.login = login;
	
	function login() {
		
		authenticateService.authenticate($.param({username: vm.username, password: vm.password}),
				function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			
			if (vm.rememberMe) {
				$localStorage.authToken = authToken;
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