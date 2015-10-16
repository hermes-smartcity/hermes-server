(function () {
  'use strict';

  angular.module('app')
    .controller('EditarMenuCtrl', ['$scope', 'menuService','$state', '$http','$stateParams', function ($scope, menuService, $state, $http, $stateParams) {
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
      
  	 // get the id
      $scope.idMenu = $stateParams.idMenu;
      var urlGet = "menu/json/getMenu?idMenu="+$scope.idMenu;
      $http.get(urlGet).success(function(data) {
			$scope.menu = data;
			$scope.nombre = $scope.menu.nombre;
			$scope.entradasMenu = $scope.menu.entradasMenu;
			console.log("--- "+$scope.entradasMenu.lenght);
      });
      
//      var urlGet = "menu/json/entradasMenu?idMenu="+$scope.idMenu;
//      $http.get(urlGet).success(function(data) {
//			$scope.entradasMenu = data;
//      });
    }]);

}());
