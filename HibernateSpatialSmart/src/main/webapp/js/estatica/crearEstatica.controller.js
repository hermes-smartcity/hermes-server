(function () {
  'use strict';

  angular.module('app')
    .controller('CrearEstaticaCtrl', ['$scope', 'estaticaService','$state', function ($scope, estaticaService, $state) {
         
      $scope.enviar = function enviar() {
			// Se llama a la funcion menu servicio y se guarda el menu
			var estatica = {'titulo':$scope.titulo,'contenido':$scope.contenido};
			estaticaService.crearEstatica(estatica);
			$state.go('inicio');
		}

		// Nombre del menu obligatorio 
      $scope.validarDatos = function validarDatos() {
			if (angular.isUndefinedOrNull($scope.titulo))
				return false;
			return true;
      }
      
      $scope.titulo = '';
      
      $scope.contenido = 'Contenido de la página';
     
     
    }]);

}());
