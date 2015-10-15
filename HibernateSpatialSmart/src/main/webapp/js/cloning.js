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
          url: '',
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
      
      $scope.nombre = '';
     
      $scope.entradasMenu = [{
        'texto': 'tree2 - item1',
        'url': 'url - item2',
        'entradasMenu': []
      }, {
        'texto': 'tree2 - item2',
        'url': 'url - item2',
        'entradasMenu': []
      }, {
        'texto': 'tree2 - item3',
        'url': 'url - item3',
        'entradasMenu': []
      }, {
        'texto': 'tree2 - item4',
        'url': 'url - item4',
        'entradasMenu': []
      }];
    }]);

}());
