(function () {
  'use strict';

  angular.module('app')
    .controller('EditarEstaticaCtrl', ['$scope', 'estaticaService','$state', '$http','$stateParams', function ($scope, estaticaService, $state, $http, $stateParams) {

      
      $scope.enviar = function enviar() {		
    	  	// Actualizo el valor del titulo del menu segun el introducido en el formulario
    	  	$scope.estatica.titulo = $scope.titulo;
    	  	$scope.estatica.contenido = $scope.contenido;
			estaticaService.editarEstatica($scope.estatica);
			$state.go('inicio');
		}

		// Nombre del estatica obligatorio 
      $scope.validarDatos = function validarDatos() {
			if (angular.isUndefinedOrNull($scope.titulo))
				return false;
			return true;
      }
      
  	 // get the id
      $scope.idEstatica = $stateParams.idEstatica;
      $scope.controladorEditar = true;
      var urlGet = "estatica/json/getEstatica?idEstatica="+$scope.idEstatica;
      $http.get(urlGet).success(function(data) {
			$scope.estatica = data;
			$scope.titulo = $scope.estatica.titulo;
			$scope.contenido = $scope.estatica.contenido;
      });
      
      $scope.borrarEstatica = function borrarEstatica(id, scope) {		
    	  	if(!angular.equals(id, null))
    	  		estaticaService.borrarEstatica(id);
			scope.remove();			
      };

    }]);

}());
