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
		'ngCookies', 'permission','ngStorage', 'googlechart',
		'pascalprecht.translate', 'tmh.dynamicLocale',
		'oitozero.ngSweetAlert', 'ngLoadingSpinner'
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
			controllerAs: 'vm'
		}).state('editUser', {
			url: '/editUser/idUser/:idUser',
			templateUrl:'partials/user/edit.html',
			controller: 'EditUserController',
			controllerAs: 'vm',
			resolve: {
				usuariosMoviles: usuariosMoviles
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
			}
		}).state('userProfile', {
			url: '/userProfile',
			templateUrl:'partials/user/userProfile.html',
			controller: 'UserProfileController',
			controllerAs: 'vm',
			resolve: {
				datosUsuario: datosUsuario
			}
		}).state('hermesServices', {
			url: '/hermesServices',
			templateUrl: 'partials/hermesServices/hermesServices.html',
			controller: 'HermesServicesController',
			controllerAs: 'vm',
			resolve: {
				services: services,
				types: types,
				dataSections: dataSections,
				eventoProcesado: eventoProcesado,
				eventsToday: eventsToday,
				statistics: statistics
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
			}
		}).state('dbconnection', {
			url: '/dbconnection',
			templateUrl: 'partials/dbconnection/dbconnection.html',
			controller: 'DBConnectionController',
			controllerAs: 'vm',
			resolve: {
				dbconnectionstype: dbconnectionstype,
				dbconnections: dbconnections
			}
		}).state('dbconcept', {
			url: '/dbconcept',
			templateUrl: 'partials/dbconcept/dbconcept.html',
			controller: 'DBConceptController',
			controllerAs: 'vm',
			resolve: {
				dbconcepts: dbconcepts
			}
		}).state('manageAttributes', {
			url: '/manageAttributes/:idConcept',
			templateUrl:'partials/dbattribute/dbattribute.html',
			controller: 'DBAttributeController',
			controllerAs: 'vm',
			resolve: {
				idConcept: function($stateParams) {
					return $stateParams.idConcept;
				},
				dbattributestype: dbattributestype,
				dbattributes: function(dbAttributeService, $stateParams) {
					return dbAttributeService.getDbAttributes($stateParams.idConcept);
				}
			}
		}).state('osmconcept', {
			url: '/osmconcept',
			templateUrl: 'partials/osmconcept/osmconcept.html',
			controller: 'OSMConceptController',
			controllerAs: 'vm',
			resolve: {
				osmconcepts: osmconcepts
			}
		}).state('manageOsmAttributes', {
			url: '/manageOsmAttributes/:idOsmConcept',
			templateUrl:'partials/osmattribute/osmattribute.html',
			controller: 'OSMAttributeController',
			controllerAs: 'vm',
			resolve: {
				idOsmConcept: function($stateParams) {
					return $stateParams.idOsmConcept;
				},
				osmattributes: function(osmAttributeService, $stateParams) {
					return osmAttributeService.getOsmAttributes($stateParams.idOsmConcept);
				}
			}
		}).state('manageOsmFilters', {
			url: '/manageOsmFilters/:idOsmConcept',
			templateUrl:'partials/osmfilter/osmfilter.html',
			controller: 'OSMFilterController',
			controllerAs: 'vm',
			resolve: {
				idOsmConcept: function($stateParams) {
					return $stateParams.idOsmConcept;
				},
				osmoperations: osmoperations,
				osmfilters: function(osmFilterService, $stateParams) {
					return osmFilterService.getOsmFilters($stateParams.idOsmConcept);
				}
			}
		}).state('job', {
			url: '/job',
			templateUrl: 'partials/job/job.html',
			controller: 'JobController',
			controllerAs: 'vm',
			resolve: {
				jobs: jobs
			}
		}).state('manageConceptTransformation', {
			url: '/manageConceptTransformation/:idJob',
			templateUrl:'partials/concepttransformation/concepttransformation.html',
			controller: 'ConceptTransformationController',
			controllerAs: 'vm',
			resolve: {
				idJob: function($stateParams) {
					return $stateParams.idJob;
				},
				concepttransformations: function(conceptTransformationService, $stateParams) {
					return conceptTransformationService.getConceptTransformations($stateParams.idJob);
				}
			}
		}).state('manageAttributeMapping', {
			url: '/manageAttributeMapping/:idConceptTransformation/:idOsmConcept/:idDbConcept',
			templateUrl:'partials/attributemapping/attributemapping.html',
			controller: 'AttributeMappingController',
			controllerAs: 'vm',
			resolve: {
				idConceptTransformation: function($stateParams) {
					return $stateParams.idConceptTransformation;
				},
				idOsmConcept: function($stateParams) {
					return $stateParams.idOsmConcept;
				},
				idDbConcept: function($stateParams) {
					return $stateParams.idDbConcept;
				},
				attributemappings: function(attributeMappingService, $stateParams) {
					return attributeMappingService.getAttributeMappings($stateParams.idConceptTransformation);
				}
			}
		}).state('executions', {
			url: '/executions',
			templateUrl: 'partials/execution/executions.html',
			controller: 'ExecutionController',
			controllerAs: 'vm',
			resolve: {
				executions: executions
			}
		}).state('showMessages', {
			url: '/showMessages/:idExecution/:status',
			templateUrl:'partials/message/messages.html',
			controller: 'MessageController',
			controllerAs: 'vm',
			resolve: {
				idExecution: function($stateParams) {
					return $stateParams.idExecution;
				},
				status: function($stateParams) {
					return $stateParams.status;
				},
				messages: function(messageService, $stateParams) {
					return messageService.getMessages($stateParams.idExecution);
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
	
	methods.$inject = ['hermesServicesService'];
	function methods(hermesServicesService) {
		return hermesServicesService.getMethods();
	}
	
	types.$inject = ['hermesServicesService'];
	function types(hermesServicesService) {
		return hermesServicesService.getTypes();
	}

	dataSections.$inject = ['hermesServicesService'];
	function dataSections(hermesServicesService) {
		return hermesServicesService.getDataSections();
	}
	
	services.$inject = ['dataServicesService'];
	function services(dataServicesService) {
		return dataServicesService.getServices();
	}
	
	dbconnectionstype.$inject = ['dbConnectionService'];
	function dbconnectionstype(dbConnectionService) {
		return dbConnectionService.getDbConnectionsType();
	}
	
	dbconnections.$inject = ['dbConnectionService'];
	function dbconnections(dbConnectionService) {
		return dbConnectionService.getDbConnections();
	}
	
	dbconcepts.$inject = ['dbConceptService'];
	function dbconcepts(dbConceptService) {
		return dbConceptService.getDbConcepts();
	}
	
	dbattributestype.$inject = ['dbAttributeService'];
	function dbattributestype(dbAttributeService) {
		return dbAttributeService.getDbAttributesType();
	}
	
	osmconcepts.$inject = ['osmConceptService'];
	function osmconcepts(osmConceptService) {
		return osmConceptService.getOsmConcepts();
	}
	
	osmoperations.$inject = ['osmFilterService'];
	function osmoperations(osmFilterService) {
		return osmFilterService.getOsmFiltersOperation();
	}
	
	jobs.$inject = ['jobService'];
	function jobs(jobService) {
		return jobService.getJobs();
	}
		
	executions.$inject = ['executionService'];
	function executions(executionService) {
		return executionService.getExecutions();
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

	angular.module('app').factory('IntervalExecutions', IntervalExecutions);

	IntervalExecutions.$inject = ['$interval'];
	function IntervalExecutions($interval) {
	  var _interval = null;

	  var service = {
	    start: start,
	    stop: stop
	  };

	  return service;

	  function start(seconds, callback) {
	    service.stop();
	    _interval = $interval(callback, 1000 * seconds);
	  }

	  function stop() {
	    if (_interval) {
	      $interval.cancel(_interval);
	      _interval = null;
	    }
	  }
	}

	angular.module('app').factory('IntervalMessages', IntervalMessages);

	IntervalMessages.$inject = ['$interval'];
	function IntervalMessages($interval) {
	  var _interval = null;

	  var service = {
	    start: start,
	    stop: stop
	  };

	  return service;

	  function start(seconds, callback) {
	    service.stop();
	    _interval = $interval(callback, 1000 * seconds);
	  }

	  function stop() {
	    if (_interval) {
	      $interval.cancel(_interval);
	      _interval = null;
	    }
	  }
	}
	
	function getUserComplete(response) {
		$rootScope.user = response.data;
		$location.path(originalPath);
	}

	appRun.$inject = ['$rootScope', '$location', '$cookieStore', 'PermissionStore', '$localStorage', 'userService', 
	                  '$state', '$translate', 'tmhDynamicLocale', 'IntervalExecutions',
	                  'IntervalMessages'];
	function appRun($rootScope, $location, $cookieStore, PermissionStore, $localStorage, 
			userService, $state, $translate, tmhDynamicLocale, IntervalExecutions,
			IntervalMessages) {

		//Paramos el manejador de peticiones para comprobar las ejecuciones de forma periodica 
		//cuando se cambia de pagina
		$rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
			IntervalExecutions.stop();    
			IntervalMessages.stop();
	    });
		
		//Configuramos el idioma por defecto
		if (angular.isDefined($localStorage.hermesmanager)) {
			if (angular.isDefined($localStorage.hermesmanager.lang)) {
				$translate.use( $localStorage.hermesmanager.lang);
				tmhDynamicLocale.set( $localStorage.hermesmanager.lang);
			}else{
				$localStorage.hermesmanager.lang = 'en';
			}
		}else{
			$localStorage.hermesmanager = {
					lang : 'en'
			};
			
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
