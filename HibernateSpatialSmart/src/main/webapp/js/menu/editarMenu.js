(function () {
  'use strict';

  angular.module('app')
    .controller('EditarMenuCtrl', ['$scope', 'menuService','$state', '$http','$stateParams', function ($scope, menuService, $state, $http, $stateParams) {

      $scope.toggle = function (scope) {
        scope.toggle();
      };

      $scope.newSubItem = function (scope) {
        var nodeData = scope.$modelValue;
        // Asigno null para que al borrar una entrada se pueda distinguir cual aun no está creada en la base de datos y cual si
        nodeData.entradasMenu.push({
          id : null,
          texto: nodeData.texto + '.' + (nodeData.entradasMenu.length + 1),
          url: 'url' + nodeData.texto + '.' + (nodeData.entradasMenu.length + 1),
          entradasMenu: []
        });
      };

      $scope.addEntradaMenuPadre = function () {
    	  var tam = $scope.entradasMenu.length+1;
    	  $scope.entradasMenu.push({
            texto: 'item'+tam,
            url: 'url - item'+tam,
            entradasMenu: []
          });
       };
        
      $scope.enviar = function enviar() {		
    	  	// Actualizo el valor del nombre del menu segun el introducido en el formulario
    	  	$scope.menu.nombre = $scope.nombre;
			menuService.editarMenu($scope.menu);
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
      $scope.controladorEditar = true;
      var urlGet = "menu/json/getMenu?idMenu="+$scope.idMenu;
      $http.get(urlGet).success(function(data) {
			$scope.menu = data;
			$scope.nombre = $scope.menu.nombre;
			$scope.entradasMenu = $scope.menu.entradasMenu;
      });
      
      $scope.borrarEntradaMenu = function borrarEntradaMenu(id, scope) {		
    	  	if(!angular.equals(id, null))
    	  		menuService.borrarEntradaMenu(id);
			scope.remove();			
      };
      
//      var urlGet = "menu/json/entradasMenu?idMenu="+$scope.idMenu;
//      $http.get(urlGet).success(function(data) {
//			$scope.entradasMenu = data;
//      });
    }]);

}());
