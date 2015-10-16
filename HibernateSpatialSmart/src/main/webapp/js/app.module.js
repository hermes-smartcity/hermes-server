(function() {
	'use strict';

	angular.module('app', [
		'ui.router',
		'ngResource',
		'ngSanitize',
		'ui.bootstrap',
		'leaflet-directive',
		'ngDraggable', 'ui.tree'
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
			templateUrl: 'partials/inicio.htm'			
		}).state('formulario', {
			url: obterRuta('formulario'),
			templateUrl: 'partials/formularioCl.htm',
			controller: 'CloningCtrl',
			controllerAs: 'cl'
		}).state('editarMenu', {
			url: obterRuta('editarMenu'),
			url: '/editarMenu/idMenu/:idMenu',
			templateUrl: 'partials/formularioCl.htm',
			controller: 'EditarMenuCtrl',
			controllerAs: 'em'
		}).state('listarMenus', {
			url: obterRuta('listarMenus'),
			templateUrl: 'partials/listarMenus.htm',
			controller: 'ListarMenusCtrl',
			controllerAs: 'lm'
		});
	}

	appRun.$inject = ['$rootScope'];
	function appRun($rootScope) {
		angular.isUndefinedOrNull = function(val) {
			return angular.isUndefined(val) || val === null;
		};

	}
})();