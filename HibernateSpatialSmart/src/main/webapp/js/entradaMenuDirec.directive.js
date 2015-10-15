(function() {
	'use strict';

	angular.module('app').directive('tree', tree);

	tree.$inject = ['RecursionHelper', '$rootScope' ];

	function tree(RecursionHelper, $rootScope) {
	    return {
	        restrict: "E",
	        scope: {block: '=', blocks: '=', indice: '=', /*calcularid:'&', contador:'='*/},
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
						var id = $rootScope.calcularid();
						console.log("esto existe? "+$rootScope.contador);
//						var id = scope.calcularid();					
						
						// TODO al estar añadiendo estoy asegurando que tiene uno al menos
						var identacion = scope.blocks[0].identacion;
					
						entradasMenuDestino.splice(scope.indice,0,
								{'orden':scope.indice,'entradasMenu':[], 'identacion':identacion,'id':id});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						
						scope.aumentarOrdenHermanos(entradasMenuDestino);
					};
					
					function addNuevoMenuEntradaHermanoAbajo() {		
						var entradasMenuDestino = scope.blocks;
						var identacion = scope.blocks[0].identacion;
						var id = $rootScope.calcularid();
						var i = scope.indice+1;
						entradasMenuDestino.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':identacion,'id':id});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						scope.aumentarOrdenHermanos(entradasMenuDestino);
			  
					};
					
					function addNuevoMenuEntradaHija(entradaMenu) {	
						var id = $rootScope.calcularid();
						var newItemNo = entradaMenu.entradasMenu.length+1;
						var identacion = entradaMenu.identacion + 50;
						entradaMenu.entradasMenu.push({'orden':newItemNo,'entradasMenu':[], 'identacion':identacion,'id':id});					
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