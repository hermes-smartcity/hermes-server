(function() {
	'use strict';
	
	angular.module('app', [
		'ui.router',
		'ngResource',
		'ngSanitize',
		'ui.bootstrap',
		'chart.js',
		'ui.bootstrap.datetimepicker',
		'ngAnimate',
		'angularUtils.directives.dirPagination',
		'ngCookies', 'permission'
	]).config(routeConfig).run(appRun);

	routeConfig.$inject = ['$stateProvider', '$urlRouterProvider', '$httpProvider'];
	function routeConfig($stateProvider, $urlRouterProvider, $httpProvider) {
	
		// TODO luego hacer states abstractos y que hereden de el
		$urlRouterProvider.otherwise('login');
		$stateProvider.state('login', {
			url: '/login',
			templateUrl:'login.html',
			controller: 'LoginController',
			controllerAs: 'vm'
		}).state('activarCuenta', { 
			url: '/activarCuenta/email/:email/hash/:hash',
			templateUrl: 'partials/user/activarCuenta.html',
			controller: 'ActivateUserController',
			controllerAs: 'vm'
		}).state('registerUser', {
			url: '/registerUser',
			templateUrl:'partials/user/register.html',
			controller: 'RegisterUserController',
			controllerAs: 'vm'
		}).state('registerAdmin', {
			url: '/registerAdmin',
			templateUrl:'partials/user/register.html',
			controller: 'RegisterAdminController',
			controllerAs: 'vm',
			data: {
			      permissions: {
			          only: ['ROLE_ADMIN'],
						redirectTo: 'login'
			        }
			}
		}).state('editUser', {
			url: '/editUser/idUser/:idUser',
			templateUrl:'partials/user/edit.html',
			controller: 'EditUserController',
			controllerAs: 'vm',
			resolve: {				
				usuariosMoviles: ['eventsService', function(eventsService) {
					return eventsService.getUsuarios();
				}]
			},
			data: {
			      permissions: {
			          only: ['ROLE_ADMIN'],
						redirectTo: 'login'
			        }
			}
		}).state('dashboard', {
			url: '/dashboard',
			templateUrl: 'partials/dashboard/dashboard.html',
			controller: 'DashboardController',
			controllerAs: 'vm',
			resolve: {
				eventsType: ['eventsService', function(eventsService) {
					return eventsService.getEvensType();
				}],
				usuarios: ['eventsService', function(eventsService) {
					return eventsService.getUsuarios();
				}],
				totalMUsers: ['userService', function(userService) {
					return userService.getTotalMUsers();
				}],
				totalWebUsers: ['userService', function(userService) {
					return userService.getTotalWebUsers();
				}],
				numberActiveUsers: ['userService', function(userService) {
					return userService.getNumberActiveUsers();
				}],
				measurementsType: ['eventsService', function(eventsService) {
					return eventsService.getMeasurementsType();
				}],
				eventoProcesado: ['eventsService', function(eventsService) {
					return eventsService.getEventoProcesado();
				}],
				eventsToday: ['eventsService', function(eventsService) {
					return eventsService.getEventsToday();
				}],				
				totalL: ['eventsService', function(eventsService) {
					return eventsService.getTotalVLocations();
				}],
				totalDS: ['eventsService', function(eventsService) {
					return eventsService.getTotalDataScts();
				}],
				totalM: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalDF: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalSTD: ['eventsService', function(eventsService) {
					return eventsService.getTotalStepsData();
				}],
				totalSLD: ['eventsService', function(eventsService) {
					return eventsService.getTotalSleepData();
				}],
				totalHRD: ['eventsService', function(eventsService) {
					return eventsService.getTotalHeartRateData();
				}],
				totalCD: ['eventsService', function(eventsService) {
					return eventsService.getTotalContextData();
				}]
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		}).state('userManager', { 
			url: '/userManager',
			templateUrl: 'partials/user/userManager.html',
			controller: 'UserManagerController',
			controllerAs: 'vm',
			resolve: {
				eventsType: ['eventsService', function(eventsService) {
					return eventsService.getEvensType();
				}],
				usuarios: ['eventsService', function(eventsService) {
					return eventsService.getUsuarios();
				}],
				measurementsType: ['eventsService', function(eventsService) {
					return eventsService.getMeasurementsType();
				}],
				totalMUsers: ['userService', function(userService) {
					return userService.getTotalMUsers();
				}],
				totalWebUsers: ['userService', function(userService) {
					return userService.getTotalWebUsers();
				}],
				numberActiveUsers: ['userService', function(userService) {
					return userService.getNumberActiveUsers();
				}],
				eventoProcesado: ['eventsService', function(eventsService) {
					return eventsService.getEventoProcesado();
				}],
				eventsToday: ['eventsService', function(eventsService) {
					return eventsService.getEventsToday();
				}],				
				totalL: ['eventsService', function(eventsService) {
					return eventsService.getTotalVLocations();
				}],
				totalDS: ['eventsService', function(eventsService) {
					return eventsService.getTotalDataScts();
				}],
				totalM: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalDF: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalSTD: ['eventsService', function(eventsService) {
					return eventsService.getTotalStepsData();
				}],
				totalSLD: ['eventsService', function(eventsService) {
					return eventsService.getTotalSleepData();
				}],
				totalHRD: ['eventsService', function(eventsService) {
					return eventsService.getTotalHeartRateData();
				}],
				totalCD: ['eventsService', function(eventsService) {
					return eventsService.getTotalContextData();
				}]
			},
			data: {
			      permissions: {
			          only: ['ROLE_ADMIN'],
						redirectTo: 'login'
			        }
			}
		}).state('eventManager', {
			url: '/eventManager',
			templateUrl:'partials/event/eventManager.html',
			controller: 'EventManagerController',
			controllerAs: 'vm',
			resolve: {
				eventsType: ['eventsService', function(eventsService) {
					return eventsService.getEvensType();
				}],
				usuarios: ['eventsService', function(eventsService) {
					return eventsService.getUsuarios();
				}],
				measurementsType: ['eventsService', function(eventsService) {
					return eventsService.getMeasurementsType();
				}],
				totalMUsers: ['userService', function(userService) {
					return userService.getTotalMUsers();
				}],
				totalWebUsers: ['userService', function(userService) {
					return userService.getTotalWebUsers();
				}],
				numberActiveUsers: ['userService', function(userService) {
					return userService.getNumberActiveUsers();
				}],
				eventoProcesado: ['eventsService', function(eventsService) {
					return eventsService.getEventoProcesado();
				}],
				eventsToday: ['eventsService', function(eventsService) {
					return eventsService.getEventsToday();
				}],				
				totalL: ['eventsService', function(eventsService) {
					return eventsService.getTotalVLocations();
				}],
				totalDS: ['eventsService', function(eventsService) {
					return eventsService.getTotalDataScts();
				}],
				totalM: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalDF: ['eventsService', function(eventsService) {
					return eventsService.getTotalMeasurements();
				}],
				totalSTD: ['eventsService', function(eventsService) {
					return eventsService.getTotalStepsData();
				}],
				totalSLD: ['eventsService', function(eventsService) {
					return eventsService.getTotalSleepData();
				}],
				totalHRD: ['eventsService', function(eventsService) {
					return eventsService.getTotalHeartRateData();
				}],
				totalCD: ['eventsService', function(eventsService) {
					return eventsService.getTotalContextData();
				}]
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		});
		
//		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		$httpProvider.interceptors.push('ErrorInterceptor');
		$httpProvider.interceptors.push('TokenAuthInterceptor');
		
	}
	
	angular.module('app').factory('ErrorInterceptor',["$q", "$rootScope", "$location", function($q, $rootScope, $location) {
		  return {
	        	'responseError': function(rejection) {
	        		var status = rejection.status;
	        		var config = rejection.config;
	        		var method = config.method;
	        		var url = config.url;
	      
	        		if (status == 401) {
	        			$location.path( "/login" );
	        			$rootScope.error="401";
	        		} if (status == 403) {
	        			$location.path( "/login" );
	        			$rootScope.error="Email/password incorrectos";
	        		} else if (status == -1){
	        			$rootScope.error = "No tienes permisos, inicia sesión con otras credenciales.";
	        		} else {
	        			$rootScope.error = method + " on " + url + " failed with status " + status;
	        		}
	              
	        		return $q.reject(rejection);
	        	}
	        };
	}]);

	
	angular.module('app').factory('TokenAuthInterceptor',["$q", "$rootScope", "$location", function($q, $rootScope, $location) {
		 return {
	        	'request': function(config) {
	        		var isRestCall = config.url.indexOf('api') > -1;
	        		if (isRestCall && angular.isDefined($rootScope.authToken)) {
	        			var authToken = $rootScope.authToken;
//	        			if (exampleAppConfig.useAuthTokenHeader) {
	        				config.headers['X-Auth-Token'] = authToken;
//	        			} else {
//	        				config.url = config.url + "?token=" + authToken;
//	        			}
	        		}
	        		return config || $q.when(config);
	        	}
	        };
	}]);
	
	function getUserComplete(response) {
		$rootScope.user = response.data;
		$location.path(originalPath);
	}
	
	appRun.$inject = ['$rootScope', '$location', '$cookieStore',  'userService', 'PermissionStore'];
	function appRun($rootScope, $location, $cookieStore, userService, PermissionStore) {
		
		angular.isUndefinedOrNull = function(val) {
			return angular.isUndefined(val) || val === null;
		};
		
		$rootScope.hasRole = function(role) {
		
			if ($rootScope.user === undefined) {
				return false;
			}
			
			if ($rootScope.user.roles[role] === undefined) {
				return false;
			}
			return $rootScope.user.roles[role];
		};
		
		$rootScope.logout = function() {
			delete $rootScope.user;
			delete $rootScope.error;
			delete $rootScope.authToken;
			$cookieStore.remove('authToken');
			$location.path("/login");
		};
		

		$rootScope.initialized = true;
		var ROLES_POSIBLES = ["ROLE_ADMIN", "ROLE_CONSULTA"];
		
		PermissionStore.defineManyPermissions(ROLES_POSIBLES, function (stateParams, permissionName) {
			  return  $rootScope.hasRole(permissionName);
		});
	

	}
})();