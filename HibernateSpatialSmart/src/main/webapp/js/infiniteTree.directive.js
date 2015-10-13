(function() {
	'use strict';

	angular.module('app').directive('block', block);

	block.$inject = ['RecursionHelper'];

	function block(RecursionHelper) {
		return {
			templateUrl: 'partials/entradaMenuTree.htm',
			 restrict: 'E',
			   /* replace: true,*/
			    scope: {
			      block: '=',
			      blockPadre: '=',
			      blocks: '=',
			      indice: '='
			      
			    },	
			    /*require: '?block',*/
			    compile: function(element) {
			    	 return RecursionHelper.compile(element, function(scope, iElement, iAttrs, controller, transcludeFn){
			            	// Define your normal link function here.
			                // Alternative: instead of passing a function,
			                // you can also pass an object with 
			                // a 'pre'- and 'post'-link function.
			            });
		        }/*,
			    link: function($scope, $element, attrs, controller) {
			    	$scope.addNuevoMenuEntradaHermanoArriba = addNuevoMenuEntradaHermanoArriba;
			 				
			    	$scope.addNuevoMenuEntradaHija = addNuevoMenuEntradaHija;
			    
			    	$scope.addNuevoMenuEntradaHermanoAbajo = addNuevoMenuEntradaHermanoAbajo;
			    	$scope.aumentarOrdenHermanos = aumentarOrdenHermanos;
					
			    	$scope.arrayDestinoAux = [];
			    	
//				    if (angular.isArray($element.entradasMenu) && $element.entradasMenu.length > 0) {
//				        $element.append('<block ng-repeat="entradasMenu in entradaMenu.entradasMenu" ng-init="blockPadre = block" indice = "indice" blocks="blocks" block="entradasMenu"></block>');
//				        $compile($element.contents().last())($scope);
//				     }
			    	
			    	function validarDatos() {
						if (angular.isUndefinedOrNull($scope.nombre))
							return false;
						return true;
					}
		
//					function removeChoice() {
//						var lastItem = hija.entradasMenu.length-1;
//						hija.entradasMenu.splice(lastItem);
//					}

					function addNuevoMenuEntradaHermanoArriba(entradaMenu, entradaMenuPadre, indice) {	
						// TODO Problema no me esta cogiendo el entradaMenuPadre ni tampoco blockPadre con el $scope. Arreglar!!					
						
						var entradasMenuDestino = [];
						var identacion;
						if(entradaMenuPadre == null){				
							entradasMenuDestino = $scope.blocks;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						entradasMenuDestino.splice($scope.indice,0,{'orden':$scope.indice,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						$scope.aumentarOrdenHermanos(entradasMenuDestino);
					};
					
					function addNuevoMenuEntradaHermanoAbajo(entradaMenu, entradaMenuPadre, indice) {		
						var entradasMenuDestino = [];
						var identacion;
						//TODO hacer funcion
						if(entradaMenuPadre == null){				
							entradasMenuDestino = $scope.blocks;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						var i = $scope.indice+1;
						entradasMenuDestino.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						$scope.aumentarOrdenHermanos(entradasMenuDestino);
			  
					};
					
					function addNuevoMenuEntradaHija(entradaMenu) {						
						var newItemNo = $scope.block.entradasMenu.length+1;
						var identacion = $scope.block.identacion + 50;
						$scope.block.entradasMenu.push({'orden':newItemNo,'entradasMenu':[], 'identacion':identacion});
					
					};
					
					// Nombre del menu obligatorio 
					function aumentarOrdenHermanos(entradasMenuAmodificar) {
						$.each(entradasMenuAmodificar, function(k, v) {
							v.orden = k;				
						});     
					}
					
			    }*/
		}
	}
})();