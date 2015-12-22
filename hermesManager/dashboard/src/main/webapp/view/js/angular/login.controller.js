(function() {
	'use strict';

	angular.module('app').controller('LoginController', LoginController);

	LoginController.$inject = ['$rootScope', '$scope', '$http', '$location', 'UserService'];

	function LoginController($rootScope, $scope, $http, $location, UserService) {
		
		
		$scope.rememberMe = false;
		
		$scope.login = function() {
			UserService.authenticate($.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
				var authToken = authenticationResult.token;
				$rootScope.authToken = authToken;
				if ($scope.rememberMe) {
					$cookieStore.put('authToken', authToken);
				}
				UserService.get(function(user) {
					$rootScope.user = user;
					$location.path("/");
				});
			});
		};
		
//		 var authenticate = function(credentials, callback) {
//
//			    var headers = credentials ? {authorization : "Basic "
//			        + btoa(credentials.username + ":" + credentials.password)
//			    } : {};
//
//			    $http.get('user', {headers : headers}).success(function(data) {
//			      if (data.name) {
//			        $rootScope.authenticated = true;
//			      } else {
//			        $rootScope.authenticated = false;
//			      }
//			      callback && callback();
//			    }).error(function() {
//			      $rootScope.authenticated = false;
//			      callback && callback();
//			    });
//
//			  }
//
//			  authenticate();
//			  $scope.credentials = {};
//			  $scope.login = function() {
//			      authenticate($scope.credentials, function() {
//			        if ($rootScope.authenticated) {
//			          $location.path("/");
//			          $scope.error = false;
//			        } else {
//			          $location.path("/login");
//			          $scope.error = true;
//			        }
//			      });
//			  };
	 
	}
})();

// TODO para una prueba. LUEGO QUITAR Y SEPARAR EN OTRO FICHERO
var services = angular.module('app', ['ngResource']);

services.factory('UserService', function($resource) {
	
	return $resource('rest/user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},
			}
		);
});