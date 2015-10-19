(function() {
	'use strict';

	angular.module('app').factory('estaticaService', estaticaService);
	// Inyecto las dependencias
	estaticaService.$inject = ['$http', '$log', '$q'];

	// Declaro el estatica que voy a devolver con sus funciones
	function estaticaService($http, $log, $q) {
		var service = {
			crearEstatica: crearEstatica,
			editarEstatica: editarEstatica,
			borrarEstatica: borrarEstatica,
			actualizarTitulosDeEstaticas: actualizarTitulosDeEstaticas
		};

		return service;

		function crearEstatica(estatica) {			
		       var deferred = $q.defer();

               $http({
                   method: 'POST',
                   url: 'estatica/crearEstatica',
                   data: estatica,
                   headers: {
                       "Content-Type": "application/json",
                       "Accept": "text/plain, application/json"
                   }
               })
               .then(function (response) {
                   if (response.status == 200) {
                       deferred.resolve();
                   }
                   else {
                   deferred.reject("Error creando estatica: " + response.data);
                   }
               });

               return deferred.promise;
		}
				
		function borrarEstatica(id) {			
		       var deferred = $q.defer();

         $http({
             method: 'POST',
             url: 'estatica/borrarEstatica',
             data: id,
             headers: {
                 "Content-Type": "application/json",
                 "Accept": "text/plain, application/json"
             }
         })
         .then(function (response) {
             if (response.status == 200) {
                 deferred.resolve();
             }
             else {
             deferred.reject("Error borrarEstatica: " + response.data);
             }
         });

         return deferred.promise;
		}
		
		function editarEstatica(estatica) {			
		       var deferred = $q.defer();

	      $http({
	          method: 'POST',
	          url: 'estatica/editarEstatica',
	          data: estatica,
	          headers: {
	              "Content-Type": "application/json",
	              "Accept": "text/plain, application/json"
	          }
	      })
	      .then(function (response) {
	          if (response.status == 200) {
	              deferred.resolve();
	          }
	          else {
	          deferred.reject("Error editarEstatica: " + response.data);
	          }
	      });
	
	      return deferred.promise;
		}
		
		function actualizarTitulosDeEstaticas(estaticas) {			
		       var deferred = $q.defer();

         $http({
             method: 'POST',
             url: 'estatica/actualizarTitulosDeEstaticas',
             data: estaticas,
             headers: {
                 "Content-Type": "application/json",
                 "Accept": "text/plain, application/json"
             }
         })
         .then(function (response) {
             if (response.status == 200) {
                 deferred.resolve();
             }
             else {
             deferred.reject("Error actualizarTitulosDeEstaticas: " + response.data);
             }
         });

         return deferred.promise;
		}

			
	}
})();