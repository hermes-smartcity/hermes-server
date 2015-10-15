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
		
		$scope.tieneHijos = function (scope) {
			  var nodeData = scope.$modelValue;
	        if(nodeData.entradasMenu && nodeData.entradasMenu.length > 0){
	        	return true;
	        } else false;
	      };
	          
	    	
	      $scope.remove = function (scope) {
	        scope.remove();
	      };

	      $scope.toggle = function (scope) {
	        scope.toggle();
	      };

	      $scope.moveLastToTheBeginning = function () {
	        var a = $scope.entradasMenu.pop();
	        $scope.entradasMenu.splice(0, 0, a);
	      };

	      $scope.newSubItem = function (scope) {
	        var nodeData = scope.$modelValue;
	        nodeData.entradasMenu.push({
	          id: nodeData.id * 10 + nodeData.entradasMenu.length,
	          title: nodeData.title + '.' + (nodeData.entradasMenu.length + 1),
	          entradasMenu: []
	        });
	      };

	      $scope.collapseAll = function () {
	        $scope.$broadcast('collapseAll');
	      };

	      $scope.expandAll = function () {
	        $scope.$broadcast('expandAll');
	      };
	    	
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