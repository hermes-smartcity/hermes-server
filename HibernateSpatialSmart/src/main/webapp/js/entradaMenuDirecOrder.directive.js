(function() {
	'use strict';

	angular.module('app').directive('treeOrder', treeOrder);

	treeOrder.$inject = ['RecursionHelper'];

	function treeOrder(RecursionHelper) {
	    return {
	        restrict: "E",
	        scope: {block: '=', blocks: '=', indice: '='},
	        require:'?treeOrder',
	        templateUrl: 'partials/entradaMenuTreeOrdenar.htm',
	        compile: function(element) {
	            return RecursionHelper.compile(element, function(scope, iElement, iAttrs, controller, transcludeFn){
	                // Define your normal link function here.
	                // Alternative: instead of passing a function,
	                // you can also pass an object with 
	                // a 'pre'- and 'post'-link function.	 				
	            	scope.onSoltarCompletado = onSoltarCompletado;
	            	scope.onArrastrarCompletado = onArrastrarCompletado;
	            	scope.onReordenarEntr = onReordenarEntr;
	            	scope.arrastradoAnteriormente = false;
	            	scope.aumentarOrdenHermanos = aumentarOrdenHermanos;
	            	scope.validarDatos = validarDatos;
	            	scope.arrayDestinoAux = [];
	            	
	            	scope.isActivada = false;			    	
			    	scope.activarClase = activarClase;
			    	scope.desactivarClase = desactivarClase;
			    	
			    	function activarClase() {
			    		scope.isActivada = true;
			    	};
			    	
			    	function desactivarClase() {
			    		scope.isActivada = false;
			    	};
	            	
			    	function validarDatos() {
						if (angular.isUndefinedOrNull(scope.nombre))
							return false;
						return true;
					}
			    	
			    	// Arrastrar
					function onArrastrarCompletado (data, evt, idEntrada){
						console.log("--blocks -onArrastrarCompletado-- "+scope.blocks[0].texto);
//						console.log("data "+data.id+" -idEntrada- "+idEntrada);
//						
//						if(angular.equals(data.id, idEntrada)){						
//				        	console.log("--- arrastrar completado --------------------------------");
//				        	var entradasMenuDestino = scope.blocks;
//				        	// Para el que arrastro lo elimino de la lista, lo estoy moviendo a otra parte
//							var index = entradasMenuDestino.indexOf(data);
//				            if (index > -1) {
//				            	scope.arrastradoAnteriormente = true; 				           	 
//					           	entradasMenuDestino.splice(index, 1);
//				            }
//						}
				     }
				  
					// Soltar en ... Se añade
					function onSoltarCompletado(data, evt, indice, idEntrada){
						console.log("--blocks -onSoltarCompletado-- "+scope.blocks[0].texto+" -- --"+evt.pageY);
						if(scope.isActivada){
							console.log("activada!!!!!!!! "+scope.blocks[0].texto);
						}
//						if(angular.equals(data.id, idEntrada)){
//					       	console.log("--- soltar completado  "+data.id+" -idEntrada-  "+idEntrada+"-----------------------");
//					       	var entradasMenuDestino = scope.blocks;
//					       	// Se supone que si se suelta en un blocks, al menos tiene un elemento
//					       	var identacion = scope.blocks[0].identacion;
//								
//					       	 var index = entradasMenuDestino.indexOf(data);
//					       	 
//					       	 // El elemento no existe en la lista destino y por lo tanto se añade y se borra de la lista original. Se decidio crear dos variables auxiliares porque sino se borra antes el elemento
//					       	 // de que se puedan hacer estas comprobaciones, tras el evento que lanza esta funcion
//							 if (index == -1){
//								 data.identacion = identacion;
//								 data.orden = indice;
//								 entradasMenuDestino.splice(indice, 0, data);
//								 
//								 // El elemento no existe en 
//								 if(scope.arrastradoAnteriormente && (scope.arrayDestinoAux.length > 0)){
//									 var indiceAux =  scope.arrayDestinoAux.indexOf(data);
//									 scope.arrayDestinoAux.splice(indiceAux, 1);	  
//								 }
//								 
//	//							 // Se actualizan los ordenes de los elementos de la lista
//								 scope.aumentarOrdenHermanos(entradasMenuDestino);				
//								 
//							} else {
//								// Si ya existe significa que hay que reordenar dentro de la lista
//								scope.onReordenarEntr(indice, data, entradasMenuDestino);
//							}
//						}
			        }
				        
					// Reordenar entradas menu
					function onReordenarEntr(index, obj, entradasMenuDestino) {						
						var otherObj = entradasMenuDestino[index];
						var otherIndex = entradasMenuDestino.indexOf(obj);
						console.log("---- intento reordenar "+index+" --otherIndex--"+otherIndex);
						entradasMenuDestino[index] = obj;
						entradasMenuDestino[index].orden = index;
						entradasMenuDestino[otherIndex] = otherObj;
						entradasMenuDestino[otherIndex].orden = otherIndex;
					};
				    
			        var inArray = function(array, obj) {
			            var index = array.indexOf(obj);
			        }			        

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