(function () {
	  'use strict';

	  angular.module('app')
	    .controller('ListarEstaticasCtrl', ['$scope', 'estaticaService','$state', '$http', function ($scope, estaticaService, $state, $http) {
	     
	      var urlGet = "estatica/json/estaticas";
	      $http.get(urlGet).success(function(data) {
				$scope.estaticas = data;
	      });
	      
	      $scope.enviar = function enviar() {			
				estaticaService.actualizarTitulosDeEstaticas($scope.estaticas);
				$state.go('inicio');
	      };
	      
	      $scope.borrarEstatica = function borrarEstatica(id, scope) {			
				estaticaService.borrarEstatica(id);
				scope.remove();
				
	      };
		      
	    }]);

	}());
