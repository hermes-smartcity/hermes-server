(function() {
	'use strict';

	angular.module('app').factory('menuService', menuService);
	// Inyecto las dependencias
	menuService.$inject = ['$http', '$log', '$q'];

	// Declaro el menu que voy a devolver con sus funciones
	function menuService($http, $log, $q) {
		var service = {
			guardarMenu: guardarMenu,
			actualizarNombresDeMenus: actualizarNombresDeMenus,
			borrarMenu: borrarMenu,
			editarMenu: editarMenu,
			borrarEntradaMenu: borrarEntradaMenu
		};

		return service;

		function guardarMenu(menu) {			
		       var deferred = $q.defer();

               $http({
                   method: 'POST',
                   url: 'menu/guardarMenu',
                   data: menu,
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
                   deferred.reject("Error saving: " + response.data);
                   }
               });

               return deferred.promise;
		}
		
		function actualizarNombresDeMenus(menus) {			
		       var deferred = $q.defer();

            $http({
                method: 'POST',
                url: 'menu/actualizarNombresDeMenus',
                data: menus,
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
                deferred.reject("Error actualizarNombresDeMenus: " + response.data);
                }
            });

            return deferred.promise;
		}
		
		function borrarMenu(id) {			
		       var deferred = $q.defer();

         $http({
             method: 'POST',
             url: 'menu/borrarMenu',
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
             deferred.reject("Error borrarMenu: " + response.data);
             }
         });

         return deferred.promise;
		}
		
		function editarMenu(menu) {			
		       var deferred = $q.defer();

	      $http({
	          method: 'POST',
	          url: 'menu/editarMenu',
	          data: menu,
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
	          deferred.reject("Error editarMenu: " + response.data);
	          }
	      });
	
	      return deferred.promise;
		}
		
		function borrarEntradaMenu(id) {			
		       var deferred = $q.defer();

	      $http({
	          method: 'POST',
	          url: 'menu/borrarEntradaMenu',
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
	          deferred.reject("Error borrarEntradaMenu: " + response.data);
	          }
	      });
	
	      return deferred.promise;
		}
			
	}
})();