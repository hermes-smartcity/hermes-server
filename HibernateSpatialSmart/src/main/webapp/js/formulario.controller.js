(function() {
	'use strict';

	angular.module('app').controller('FormularioController', FormularioController);

	FormularioController.$inject = ['$state', '$q', 'menuService', '$scope' ];

	function FormularioController($state, $q, menuService, $scope) {
		var vm = this;
		$scope.entradasMenu = [];
		//TODO cambiar a que valide correctamente!
		$scope.nombre = '';
//		vm.menu = {};
		vm.addNuevoMenuEntrada = addNuevoMenuEntrada;
		vm.removeChoice = removeChoice;
		vm.validarDatos = validarDatos;
		vm.enviar = enviar;
		vm.addNuevoMenuEntradaHija = addNuevoMenuEntradaHija;
		vm.addNuevoMenuEntradaHermanoArriba = addNuevoMenuEntradaHermanoArriba;
		vm.addNuevoMenuEntradaHermanoAbajo = addNuevoMenuEntradaHermanoAbajo;
		
		// Entradas Menus		
		function addNuevoMenuEntrada() {
			var newItemNo = $scope.entradasMenu.length+1;
			// "Chapucilla momentánea" falta decidir como se va a hacer
			$scope.entradasMenu.push({'orden':newItemNo,'entradasMenu':[],'identacion':50});
//			var menu = {'nombre':$scope.nombre,'entradasMenu':$scope.entradasMenu};
//			menuService.guardarMenu(menu);

		}

		function removeChoice() {
			var lastItem = $scope.entradasMenu.length-1;
			$scope.entradasMenu.splice(lastItem);
		}

		function addNuevoMenuEntradaHermanoArriba(entradaMenu, entradasMenu, indice) {			
			entradasMenu.splice(indice,0,{'orden':indice,'entradasMenu':[], 'identacion':entradaMenu.identacion})
		};
		
		function addNuevoMenuEntradaHermanoAbajo(entradaMenu, entradasMenu, indice) {
			var i = indice+1;//Recorre el nuevo array
			entradasMenu.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':entradaMenu.identacion})
		};
		
		function addNuevoMenuEntradaHija(entradaMenu) {
			var newItemNo = entradaMenu.entradasMenu.length+1;
			var identacion = entradaMenu.identacion + 50;
			entradaMenu.entradasMenu.push({'orden':newItemNo,'entradasMenu':[], 'identacion':identacion});
		};

		function addHijo(index) {
			var newItemNo = $scope.entradasMenu[index].entradasMenu.length+1;
			$scope.entradasMenu[index].entradasMenu.push({'orden':newItemNo,'entradasMenu':[]});

		};

		// Reordenar entradas menu
		function onDropCompleteEntr(index, obj, evt) {
			var otherObj = $scope.entradasMenu[index];
			var otherIndex = $scope.entradasMenu.indexOf(obj);
			$scope.entradasMenu[index] = obj;
			$scope.entradasMenu[index].orden = index;
			$scope.entradasMenu[otherIndex] = otherObj;
			$scope.entradasMenu[otherIndex].orden = otherIndex;
		};

		function enviar() {
			//se llama a la funcion menu servicio y se guarda el menu
			var menu = {'nombre':$scope.nombre,'entradasMenu':$scope.entradasMenu};
			menuService.guardarMenu(menu);
			$state.go('inicio');
		}

		// Nombre del menu obligatorio 
		function validarDatos() {
			if (angular.isUndefinedOrNull($scope.nombre))
				return false;
			return true;
		}

	}
})();