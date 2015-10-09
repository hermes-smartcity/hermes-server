(function() {
	'use strict';

	angular.module('app').directive('blockReordenar', blockReordenar);

	blockReordenar.$inject = ['$compile'];

	function blockReordenar($compile) {
		return {
			templateUrl: 'partials/entradaMenuTreeReordenar.htm',
			 restrict: 'E',
			    replace: true,
			    require: '^blockReordenar',
			    scope: {
			      blockReordenar: '='
			    },
			    controller: function ($scope) {
			    	var hija = this;
			    	hija.validarDatos = validarDatos;
			    	hija.onSoltarCompletado = onSoltarCompletado;
			    	hija.onArrastrarCompletado = onArrastrarCompletado;
			    	hija.onReordenarEntr = onReordenarEntr;
					
			    	hija.arrastradoAnteriormente = false;
					
					function validarDatos() {
						if (angular.isUndefinedOrNull(vm.nombre))
							return false;
						return true;
					}
					
					
//					ng-drop="true"  ng-drop-success="f.onSoltarCompletado($data, $event, entradaMenuPadre,  $index)"
				},
			    link: function($scope, $element) {
			      if (angular.isArray($scope.entradaMenu.entradasMenu) && $scope.entradaMenu.entradasMenu.length > 0) {
			        $element.append('<blockReordenar ng-repeat="entradasMenu in entradaMenu.entradasMenu"  blockReordenar="entradasMenu"></blockReordenar>');
			        $compile($element.contents().last())($scope);
			      }
			    }
		}
	}
})();