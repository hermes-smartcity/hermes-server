(function() {
	'use strict';

	angular.module('app', [
		'ui.router',
		'ngResource',
		'ngSanitize',
		'ui.bootstrap',
		'chart.js',
		'ui.bootstrap.datetimepicker'
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
		$stateProvider.state('inicio', {
			url: obterRuta('inicio'),
			templateUrl: 'partials/events/inicio.htm',
			controller: 'DashboardController',
			controllerAs: 'vm'
		}).state('eventManager', {
			url: obterRuta('eventManager'),
			templateUrl:'partials/events/eventManager.htm',
			controller: 'EventManagerController',
			controllerAs: 'vm'
		});
	}

	appRun.$inject = ['$rootScope'];
	function appRun($rootScope) {
		angular.isUndefinedOrNull = function(val) {
			return angular.isUndefined(val) || val === null;
		};

	}
})();