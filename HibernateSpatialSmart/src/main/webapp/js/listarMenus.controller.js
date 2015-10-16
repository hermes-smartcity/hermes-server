(function () {
	  'use strict';

	  angular.module('app')
	    .controller('ListarMenusCtrl', ['$scope', 'menuService','$state', '$http', function ($scope, menuService, $state, $http) {
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

	      var urlGet = "menu/json/menus";
	      $http.get(urlGet).success(function(data) {
				$scope.menus = data;
	      });
	      
	    }]);

	}());
