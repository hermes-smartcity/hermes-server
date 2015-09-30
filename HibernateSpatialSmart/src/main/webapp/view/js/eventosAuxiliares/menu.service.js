(function() {
	'use strict';

	angular.module('menuApp').factory('menuService', menuService);
	// Inyecto las dependencias
	menuService.$inject = ['$http', '$log', '$q'];

	// Declaro el menu que voy a devolver con sus funciones
	function menuService($http, $log, $q) {
		var service = {
			guardarMenu: guardarMenu
		};

		return service;

		function guardarMenu(menu) {
			console.log("incompleto");
			//INCOMPLETO
		       var deferred = $q.defer();

               $http({
                   method: 'POST',
                   url: 'guardarMenu',
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
	}
})();