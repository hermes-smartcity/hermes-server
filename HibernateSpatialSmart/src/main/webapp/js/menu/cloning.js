(function () {
  'use strict';

  angular.module('app')
    .controller('CloningCtrl', ['$scope', 'menuService','$state', function ($scope, menuService, $state) {
      $scope.remove = function (scope) {
        scope.remove();
      };

      $scope.toggle = function (scope) {
        scope.toggle();
      };

      $scope.newSubItem = function (scope) {
        var nodeData = scope.$modelValue;
        nodeData.entradasMenu.push({
          texto: nodeData.texto + '.' + (nodeData.entradasMenu.length + 1),
          url: 'url' + nodeData.texto + '.' + (nodeData.entradasMenu.length + 1),
          entradasMenu: []
        });
      };

      
      $scope.enviar = function enviar() {
			// Se llama a la funcion menu servicio y se guarda el menu
			var menu = {'nombre':$scope.nombre,'entradasMenu':$scope.entradasMenu};
			menuService.guardarMenu(menu);
			$state.go('inicio');
		}

		// Nombre del menu obligatorio 
      $scope.validarDatos = function validarDatos() {
			if (angular.isUndefinedOrNull($scope.nombre))
				return false;
			return true;
      }
      
      // TODO falta pensar si es la mejor solucion -> 1 html y dos controladores o 2 htmls y dos controladores. Esta variable se utiliza para saber a que funcion eliminar se llama.
      // En crear se elimina esa entrada del scope (no está ni siquiera creada la entrada de menu), en editar hay que llamar a un service y al Dao para que la elimine de la BD.
      $scope.controladorEditar = false;
      
      $scope.nombre = '';
     
      $scope.entradasMenu = [{
        'texto': 'item1',
        'url': 'url - item2',
        'entradasMenu': []
      }, {
        'texto': 'item2',
        'url': 'url - item2',
        'entradasMenu': []
      }, {
        'texto': 'item3',
        'url': 'url - item3',
        'entradasMenu': []
      }, {
        'texto': 'item4',
        'url': 'url - item4',
        'entradasMenu': []
      }];
    }]);

}());
