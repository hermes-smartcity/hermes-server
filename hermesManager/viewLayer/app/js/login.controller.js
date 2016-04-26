(function() {
	'use strict';

	angular.module('app').controller('LoginController', LoginController);

	LoginController.$inject = ['$rootScope','$scope', '$http', '$location','$state', 'authenticateService', 'userService', '$localStorage', 'eventsService'];

	function LoginController($rootScope, $scope, $http, $location, $state, authenticateService, userService, $localStorage, eventsService) {

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

				//Calculamos e valor de eventsType, measurementTypes, sensor type
				//y lo almacenamos en la variable local
				eventsService.getEvensType().then(getEventsTypeComplete);
				function getEventsTypeComplete(response){
					$rootScope.eventsType = response;

					eventsService.getMeasurementsType().then(getMeasurementsTypeComplete);
					function getMeasurementsTypeComplete(response){
						$rootScope.measurementsType = response;

						
						eventsService.getSensorsType().then(getSensorsTypeComplete);
						function getSensorsTypeComplete(response){
							$rootScope.sensorsType = response;
						
							userService.getUser(url_get_user).then(getUserComplete);
							function getUserComplete(response) {
								$rootScope.user = response.data;
								$location.path("/");
								$state.go("dashboard");				
							}
						}
						
					}

				}

			});

		}

	}
})();