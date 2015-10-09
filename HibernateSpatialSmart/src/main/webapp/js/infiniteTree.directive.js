(function() {
	'use strict';

	angular.module('app').directive('block', block);

	block.$inject = ['$compile'];

	function block($compile) {
		return {
			templateUrl: 'partials/entradaMenuTree.htm',
			 restrict: 'E',
			    replace: true,
			    scope: {
			      block: '='
			    },			   
			    link: function($scope, $element, attrs, controller) {
			    					
			    	$scope.addNuevoMenuEntradaHermanoArriba = addNuevoMenuEntradaHermanoArriba;
			 				
			    	$scope.addNuevoMenuEntradaHija = addNuevoMenuEntradaHija;
			    
			    	$scope.addNuevoMenuEntradaHermanoAbajo = addNuevoMenuEntradaHermanoAbajo;
			    	$scope.aumentarOrdenHermanos = aumentarOrdenHermanos;
					
			    	$scope.arrayDestinoAux = [];
			    	
			    	// TODO DUDA IMPORTANTE EL SCOPE QUE ME PASAN POR PARAMETRO ES EL GENERAL??
					function validarDatos() {
						if (angular.isUndefinedOrNull(hija.nombre))
							return false;
						return true;
					}
		
//					function removeChoice() {
//						var lastItem = hija.entradasMenu.length-1;
//						hija.entradasMenu.splice(lastItem);
//					}

					function addNuevoMenuEntradaHermanoArriba(entradaMenu, entradaMenuPadre, indice) {		
						console.log("-----addNuevoMenuEntradaHermanoArriba------- "+controller.entradasMenu.length);
						var entradasMenuDestino = [];
						var identacion;
						if(entradaMenuPadre == null){				
							entradasMenuDestino = hija.entradasMenu;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						entradasMenuDestino.splice(indice,0,{'orden':indice,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						hija.aumentarOrdenHermanos(entradasMenuDestino);
					};
					
					function addNuevoMenuEntradaHermanoAbajo(entradaMenu, entradaMenuPadre, indice) {		
						console.log("----addNuevoMenuEntradaHermanoAbajo-------- "+controller.length);
						var entradasMenuDestino = [];
						var identacion;
						//TODO hacer funcion
						if(entradaMenuPadre == null){				
							entradasMenuDestino = hija.entradasMenu;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						var i = indice+1;
						entradasMenuDestino.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						hija.aumentarOrdenHermanos(entradasMenuDestino);
			  
					};
					
					function addNuevoMenuEntradaHija(entradaMenu) {
						var newItemNo = entradaMenu.entradasMenu.length+1;
						var identacion = entradaMenu.identacion + 50;
						entradaMenu.entradasMenu.push({'orden':newItemNo,'entradasMenu':[], 'identacion':identacion});
					};
					
					// Nombre del menu obligatorio 
					function aumentarOrdenHermanos(entradasMenuAmodificar) {
						$.each(entradasMenuAmodificar, function(k, v) {
							v.orden = k;				
						});     
					}
					
			      if (angular.isArray($element.entradasMenu) && $element.entradasMenu.length > 0) {
			    	  console.log("entro por aki?");
			        $element.append('<block ng-repeat="entradasMenu in entradaMenu.entradasMenu" block="entradasMenu"></block>');
			        $compile($element.contents().last())($scope);
			      }
			    }
		}
	}
})();