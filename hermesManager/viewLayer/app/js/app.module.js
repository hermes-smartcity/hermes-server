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
		'datatables',
		'ngCookies', 'permission','ngStorage', 
		'pascalprecht.translate', 'tmh.dynamicLocale'
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
				usuariosMoviles: usuariosMoviles
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
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
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
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
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
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		}).state('systemLogs', {
			url: '/systemLogs',
			templateUrl:'partials/systemLogs/systemLogs.html',
			controller: 'SystemLogsController',
			controllerAs: 'vm',
			resolve: {
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
			},
			data: {
			      permissions: {
			          only: ['ROLE_ADMIN'],
						redirectTo: 'login'
			        }
			}
		}).state('changePassword', {
			url: '/changePassword',
			templateUrl:'partials/user/changePassword.html',
			controller: 'ChangePasswordController',
			controllerAs: 'vm'
		}).state('settings', {
			url: '/settings',
			templateUrl:'partials/settings/settings.html',
			controller: 'SettingsController',
			controllerAs: 'vm',
			resolve: {
				datosSettings: datosSettings
			},
			data: {
			      permissions: {
			          only: ['ROLE_ADMIN'],
						redirectTo: 'login'
			        }
			}
		}).state('userProfile', {
			url: '/userProfile',
			templateUrl:'partials/user/userProfile.html',
			controller: 'UserProfileController',
			controllerAs: 'vm',
			resolve: {
				datosUsuario: datosUsuario
			}
		}).state('smartdriver', {
			url: '/smartdriver',
			templateUrl: 'partials/smartdriver/smartdriver.html',
			controller: 'SmartDriverController',
			controllerAs: 'vm',
			resolve: {
				methods: methods,
				types: types,
				dataSections: dataSections,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		}).state('dataServices', {
			url: '/dataServices',
			templateUrl:'partials/dataServices/dataServices.html',
			controller: 'DataServicesController',
			controllerAs: 'vm',
			resolve: {
				services: services,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		}).state('sensorData', {
			url: '/sensorData',
			templateUrl:'partials/sensorData/sensorData.html',
			controller: 'SensorDataController',
			controllerAs: 'vm',
			resolve: {
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
			},
			data: {
			      permissions: {
			    	  only: ['ROLE_ADMIN', 'ROLE_CONSULTA'],
						redirectTo: 'login'
			        }
			}
		}).state('gpsLocation', {
			url: '/gpsLocation',
			templateUrl: 'partials/gpsLocation/gpsLocation.html',
			controller: 'GPSLocationController',
			controllerAs: 'vm',
			resolve: {
				usuarios: usuarios,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
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

	
	usuariosMoviles.$inject = ['eventsService'];
	function usuariosMoviles(eventsService) {
	    return eventsService.getUsuarios();
	}
	
	usuarios.$inject = ['eventsService'];
	function usuarios(eventsService) {
		return eventsService.getUsuarios();
	}
	
	eventoProcesado.$inject = ['eventsService'];
	function eventoProcesado(eventsService) {
		return eventsService.getEventoProcesado();
	}
	
	eventsToday.$inject = ['eventsService'];
	function eventsToday(eventsService) {
		return eventsService.getEventsToday();
	}
	
	statistics.$inject = ['userService'];
	function statistics(userService) {
		return userService.getParametersStatistics();
	}
	
	datosSettings.$inject = ['settingsService'];
	function datosSettings(settingsService) {
		return settingsService.getSettings();
	}
	
	datosUsuario.$inject = ['userService'];
	function datosUsuario(userService) {
		return userService.getUserProfile();
	}
	
	methods.$inject = ['smartDriverService'];
	function methods(smartDriverService) {
		return smartDriverService.getMethods();
	}
	
	types.$inject = ['smartDriverService'];
	function types(smartDriverService) {
		return smartDriverService.getTypes();
	}
	
	dataSections.$inject = ['smartDriverService'];
	function dataSections(smartDriverService) {
		return smartDriverService.getDataSections();
	}
	
	services.$inject = ['dataServicesService'];
	function services(dataServicesService) {
		return dataServicesService.getServices();
	}
	
	angular.module('app').config(translateAppConfig);
	translateAppConfig.$inject = ['$translateProvider'];
	function translateAppConfig($translateProvider) {
		$translateProvider.preferredLanguage('en');
		$translateProvider.useSanitizeValueStrategy('escaped');
	}
	
	angular.module('app').config(dynamicConfig);
	dynamicConfig.$inject = ['tmhDynamicLocaleProvider', '$httpProvider'];
	function dynamicConfig(tmhDynamicLocaleProvider, $httpProvider) {
		tmhDynamicLocaleProvider.localeLocationPattern("./translations/angular-locale_{{ locale }}.js");
		  $httpProvider.defaults.headers.common = {};
		  $httpProvider.defaults.headers.post = {};
		  $httpProvider.defaults.headers.put = {};
		  $httpProvider.defaults.headers.patch = {};
	}
	
	angular.module('app').factory('ErrorInterceptor',["$q", "$rootScope", "$location", '$translate', function($q, $rootScope, $location, $translate) {
		  return {
	        	'responseError': function(rejection) {
	        		var status = rejection.status;
	        		var config = rejection.config;
	        		var method = config.method;
	        		var url = config.url;

	        		if (status == 401) {
	        			$location.path("/login");
	        			$rootScope.error="401";
						delete $rootScope.user;
						delete $rootScope.authToken;
	        		} if (status == 403) {
	        			$location.path("/login");
	        			$rootScope.error=  $translate.instant('emailPasswordIncorrectos'); 
	        		} else if (status == -1){
								delete $rootScope.user;
								delete $rootScope.error;
								delete $rootScope.authToken;
								$location.path("/login");
	        		} else {
	        			$rootScope.error = method + $translate.instant('en') + url + $translate.instant('falloConEstado') + status;
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

	        			config.headers['X-Auth-Token'] = authToken;

//	        			if (exampleAppConfig.useAuthTokenHeader) {
	        			//	config.headers['X-Auth-Token'] = authToken;
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

	appRun.$inject = ['$rootScope', '$location', '$cookieStore', 'PermissionStore', '$localStorage', 'userService', 
	                  '$state', '$translate', 'tmhDynamicLocale'];
	function appRun($rootScope, $location, $cookieStore, PermissionStore, $localStorage, 
			userService, $state, $translate, tmhDynamicLocale) {

		//Configuramos el idioma por defecto
		if (angular.isDefined($localStorage.hermesmanager.lang)) {
			$translate.use( $localStorage.hermesmanager.lang);
			tmhDynamicLocale.set( $localStorage.hermesmanager.lang);
		}else{
			$localStorage.hermesmanager.lang = 'en';
		}
		
		//Si existe el token guardado en $localStorage y no ha caducado aun, se renueva
		if ($localStorage.authToken){
			//para que al hacer TokenAuthInterceptor tengo el valor
			$rootScope.authToken = $localStorage.authToken;

			var res = $localStorage.authToken.split(":");
			var expires = res[1];
			var time = parseFloat(expires);

			var d = new Date();
			var timeActual = d.getTime();

			if (time >= timeActual) {

				userService.renewToken($localStorage.authToken).then(function(response){
					//Asignamos el nuevo valor de token
					$localStorage.authToken = response.token;

					//Redirigimos a la pantalla de dashboard
					userService.getUser(url_get_user).then(function(response){
						$rootScope.user = response.data;
						$location.path("/");
						$state.go("dashboard");
					});
				});

			}
		}

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
			delete $localStorage.authToken;

			$location.path("/login");
		};


		$rootScope.initialized = true;
		var ROLES_POSIBLES = ["ROLE_ADMIN", "ROLE_CONSULTA"];

		PermissionStore.defineManyPermissions(ROLES_POSIBLES, function (stateParams, permissionName) {
			  return  $rootScope.hasRole(permissionName);
		});


	}
})();
