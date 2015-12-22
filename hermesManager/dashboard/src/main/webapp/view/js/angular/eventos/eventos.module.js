(function() {
	'use strict';

	angular.module('eventos', ['ui.router',
	                   		'ngResource',
	                		'ngSanitize',
	                		'ui.bootstrap',
	                		'chart.js',
	                		'ui.bootstrap.datetimepicker',
	                		'ngAnimate',
	                		'angularUtils.directives.dirPagination'
	                		]);

	function obterRuta(ruta) {
		if (no_concrete_routing) {
			return '/';
		} else {
			return '/' + ruta;
		}
	}
	
	angular.module('eventos').config(function($stateProvider, $urlRouterProvider, $locationProvider, $provide) {

		$stateProvider.state('inicio', {
			url: obterRuta('inicio'),
			templateUrl: 'partials/events/inicio.htm',
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
			templateUrl:'partials/events/eventManager.htm',
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
		}).state('login', {
			url: obterRuta('login'),
			templateUrl:'partials/login/login.htm',
			controller: 'LoginController',
			controllerAs: 'vm'/*,
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
			}*/
		}); 

		
	});

})();