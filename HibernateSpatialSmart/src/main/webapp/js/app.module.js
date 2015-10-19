(function() {
	'use strict';

	angular.module('app', [
		'ui.router',
		'ngResource',
		'ngSanitize',
		'ui.bootstrap',
		'leaflet-directive',
		'ui.tree','textAngular'
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
		}).state('crearEstatica', {
			url: obterRuta('crearEstatica'),
			templateUrl: 'partials/formularioEstatica.htm',
			controller: 'CrearEstaticaCtrl',
			controllerAs: 'ce'
		}).state('editarEstatica', {
			url: '/editarEstatica/idEstatica/:idEstatica',
			templateUrl: 'partials/formularioEstatica.htm',
			controller: 'EditarEstaticaCtrl',
			controllerAs: 'ee'
		}).state('listarEstaticas', {
			url: obterRuta('listarEstaticas'),
			templateUrl: 'partials/listarEstaticas.htm',
			controller: 'ListarEstaticasCtrl',
			controllerAs: 'le'
		}).state('formulario', {
			url: obterRuta('formulario'),
			templateUrl: 'partials/formularioCl.htm',
			controller: 'CloningCtrl',
			controllerAs: 'cl'
		}).state('editarMenu', {			
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