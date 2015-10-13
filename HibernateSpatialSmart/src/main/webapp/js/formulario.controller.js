(function() {
	'use strict';

	angular.module('app').controller('FormularioController', FormularioController);

	FormularioController.$inject = ['$state', '$q', 'menuService', '$scope' ];

	function FormularioController($state, $q, menuService, $scope) {
		var vm = this;
		$scope.entradasMenu = [];
		
		vm.nombre = '';
		vm.validarDatos = validarDatos;
		vm.enviar = enviar;
		vm.addNuevoMenuEntrada = addNuevoMenuEntrada;
		
		function addNuevoMenuEntrada() {
			var newItemNo = $scope.entradasMenu.length+1;
			// "Chapucilla momentánea" falta decidir como se va a hacer
			$scope.entradasMenu.push({'orden':newItemNo,'entradasMenu':[],'identacion':50});
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
	}
})();