(function () {
	  'use strict';

	  angular.module('app')
	    .controller('ListarMenusCtrl', ['$scope', 'menuService','$state', '$http', function ($scope, menuService, $state, $http) {
	    
	      var urlGet = "menu/json/menus";
	      $http.get(urlGet).success(function(data) {
				$scope.menus = data;
	      });
	      
	      $scope.enviar = function enviar() {			
				menuService.actualizarNombresDeMenus($scope.menus);
				$state.go('inicio');
	      };
	      
	      $scope.borrarMenu = function borrarMenu(id, scope) {			
				menuService.borrarMenu(id);
				scope.remove();
				
	      };
		      
	    }]);

	}());
