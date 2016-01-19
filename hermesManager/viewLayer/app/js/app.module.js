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
		'angularUtils.directives.dirPagination'
	]).config(routeConfig).run(appRun);

	function obterRuta(ruta) {
		if (no_concrete_routing) {
			return '/';
		} else {
			return '/' + ruta;
		}
	}

	routeConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
	function routeConfig($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise(obterRuta('inicio'));
		$stateProvider./*state('login', {
			url: obterRuta('login'),
			templateUrl:'login.html',
			controller: 'LoginController'
		}).*/state('inicio', {
			url: obterRuta('inicio'),
			templateUrl: 'partials/dashboard.html',
			controller: 'DashboardController',
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
				}]
			}
		}).state('eventManager', {
			url: obterRuta('eventManager'),
			templateUrl:'partials/eventManager.html',
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
				}]
			}
		});
	}

	appRun.$inject = ['$rootScope'];
	function appRun($rootScope) {
		angular.isUndefinedOrNull = function(val) {
			return angular.isUndefined(val) || val === null;
		};

	}
})();