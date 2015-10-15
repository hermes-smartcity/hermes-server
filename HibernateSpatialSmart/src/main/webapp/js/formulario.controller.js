(function() {
	'use strict';

	angular.module('app').controller('FormularioController', FormularioController);

	FormularioController.$inject = ['$state', '$q', 'menuService', '$scope', '$rootScope' ];

	function FormularioController($state, $q, menuService, $scope, $rootScope) {
		var vm = this;
		$scope.entradasMenu = [];
		$rootScope.contador = 0;
		
		vm.nombre = '';
		
		vm.validarDatos = validarDatos;
		vm.enviar = enviar;
		vm.addNuevoMenuEntrada = addNuevoMenuEntrada;
		$rootScope.calcularid = calcularid;
		
		
		function addNuevoMenuEntrada() {
			var newItemNo = $scope.entradasMenu.length+1;
			var id = $rootScope.calcularid();
//			var id = vm.calcularid();
			// "Chapucilla momentánea" falta decidir como se va a hacer
			$scope.entradasMenu.push({'orden':newItemNo,'entradasMenu':[],'identacion':50, 'id':id});
		}
		
		function enviar() {
			// Se llama a la funcion menu servicio y se guarda el menu
			var menu = {'nombre':vm.nombre,'entradasMenu':$scope.entradasMenu};
			menuService.guardarMenu(menu);
			$state.go('inicio');
		}

		// Nombre del menu obligatorio 
		function validarDatos() {
			if (angular.isUndefinedOrNull(vm.nombre))
				return false;
			return true;
		}
		
		function calcularid() {
			$rootScope.contador = $rootScope.contador + 1; 
			return $rootScope.contador;
		}
	}
})();