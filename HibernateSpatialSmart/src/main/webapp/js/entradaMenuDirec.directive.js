(function() {
	'use strict';

	angular.module('app').directive('tree', tree);

	tree.$inject = ['RecursionHelper'];

	function tree(RecursionHelper) {
	    return {
	        restrict: "E",
	        scope: {block: '=', blocks: '=', indice: '='},
	        templateUrl: 'partials/entradaMenuTree.htm',
	        compile: function(element) {
	            return RecursionHelper.compile(element, function(scope, iElement, iAttrs, controller, transcludeFn){
	                // Define your normal link function here.
	                // Alternative: instead of passing a function,
	                // you can also pass an object with 
	                // a 'pre'- and 'post'-link function.
	            	scope.addNuevoMenuEntradaHermanoArriba = addNuevoMenuEntradaHermanoArriba;
	 				
			    	scope.addNuevoMenuEntradaHija = addNuevoMenuEntradaHija;
			    
			    	scope.addNuevoMenuEntradaHermanoAbajo = addNuevoMenuEntradaHermanoAbajo;
			    	scope.aumentarOrdenHermanos = aumentarOrdenHermanos;
					
			    	scope.arrayDestinoAux = [];
					    	
			    	function validarDatos() {
						if (angular.isUndefinedOrNull(scope.nombre))
							return false;
						return true;
					}

					function addNuevoMenuEntradaHermanoArriba() {	
						var entradasMenuDestino = scope.blocks;
						//TODO al estar añadiendo estoy asegurando que tiene uno al menos
						var identacion = scope.blocks[0].identacion;
					
						entradasMenuDestino.splice(scope.indice,0,{'orden':scope.indice,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						scope.aumentarOrdenHermanos(entradasMenuDestino);
					};
					
					function addNuevoMenuEntradaHermanoAbajo() {		
						var entradasMenuDestino = scope.blocks;
						var identacion = scope.blocks[0].identacion;
						
						var i = scope.indice+1;
						entradasMenuDestino.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						scope.aumentarOrdenHermanos(entradasMenuDestino);
			  
					};
					
					function addNuevoMenuEntradaHija(entradaMenu) {						
						var newItemNo = entradaMenu.entradasMenu.length+1;
						var identacion = entradaMenu.identacion + 50;
						console.log("me estaq cogiendo entrada ?? "+identacion);
						entradaMenu.entradasMenu.push({'orden':newItemNo,'entradasMenu':[], 'identacion':identacion});
					
					};
					
					// Nombre del menu obligatorio 
					function aumentarOrdenHermanos(entradasMenuAmodificar) {
						$.each(entradasMenuAmodificar, function(k, v) {
							v.orden = k;				
						});     
					};
	            });
	        }
	    };
	}
})();