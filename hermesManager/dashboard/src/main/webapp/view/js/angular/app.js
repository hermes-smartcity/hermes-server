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
		'util'
	]).config(routeConfig).run(appRun);

	function obterRuta(ruta) {
		if (no_concrete_routing) {
			return '/';
		} else {
			return '/' + ruta;
		}
	}

	routeConfig.$inject = ['$stateProvider', '$urlRouterProvider', '$httpProvider'];
	function routeConfig($stateProvider, $urlRouterProvider, $httpProvider) {
		console.log("routing");
		$httpProvider.interceptors.push('sessionRecoverer');
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest'; 
		
		$urlRouterProvider.otherwise(obterRuta('login'));
		$stateProvider.state('login', {
			url: obterRuta('inicio'),
			templateUrl: 'partials/login.html',
			controller: LoginController
		
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
			templateUrl:'partials/login/signin.htm',
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
		// TODO Arreglar:
//		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	}

	appRun.$inject = ['$rootScope'];
	function appRun($rootScope) {
		angular.isUndefinedOrNull = function(val) {
			return angular.isUndefined(val) || val === null;
		};

	}
})();