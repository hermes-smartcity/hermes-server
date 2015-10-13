(function() {
	'use strict';

	angular.module('app').directive('treeOrder', treeOrder);

	treeOrder.$inject = ['RecursionHelper'];

	function treeOrder(RecursionHelper) {
	    return {
	        restrict: "E",
	        scope: {block: '=', blocks: '=', indice: '='},
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
	            	scope.validarDatos = validarDatos;
					    	
			    	function validarDatos() {
						if (angular.isUndefinedOrNull(scope.nombre))
							return false;
						return true;
					}
			    	
			    	 // Arrastrar
					function onArrastrarCompletado (data, event, entradaMenuPadre){
			        	console.log("--- arrastrar completado --------------------------------");
			        	console.log("--- data "+data.texto+" -entradaMenuPadre- "+entradaMenuPadre.texto);
			        	var entradasMenuDestino = [];
						var identacion;
						if(entradaMenuPadre == null){				
							entradasMenuDestino = vm.entradasMenu;				
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;					
						}
			        	// Para el que arrastro lo elimino de la lista, lo estoy moviendo a otra parte, asi que lo añado a la lista destino
						var index = entradasMenuDestino.indexOf(data);
			            if (index > -1) {
			            	vm.arrastradoAnteriormente = true;
			            	console.log("-- arrastradoAnteriormente");
			            	vm.arrayDestinoAux = entradasMenuDestino;     	
			            }	           
				     }
				  
					// Soltar en ... Se añade
					function onSoltarCompletado(data, event, entradaMenuPadre, indice){
				       	console.log("--- soltar completado  "+data.texto+" -Indice-  "+indice+" -entradaMenuPadre- "+entradaMenuPadre.texto+" --------------------------- ");
				       	var entradasMenuDestino = [];
							var identacion;
							if(entradaMenuPadre == null){	
								entradasMenuDestino = vm.entradasMenu;		
								identacion = 50;
							} else {
								entradasMenuDestino = entradaMenuPadre.entradasMenu;		
								identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
							}
							
				       	 var index = entradasMenuDestino.indexOf(data);
				       	 console.log(" -- index--"+index);
				       	 // El elemento no existe en la lista destino y por lo tanto se añade y se borra de la lista original. Se decidio crear dos variables auxiliares porque sino se borra antes el elemento
				       	 // de que se puedan hacer estas comprobaciones, tras el evento que lanza esta funcion
						 if (index == -1){
							 data.identacion = identacion;
							 data.orden = indice;
							 console.log("-- data.orden -- "+data.orden);
							 entradasMenuDestino.splice(indice,0,data);
							 // El elemento no existe en 
							 if(vm.arrastradoAnteriormente && (vm.arrayDestinoAux.length > 0)){
								 console.log("borrado");
								 var indiceAux =  vm.arrayDestinoAux.indexOf(data);
								 vm.arrayDestinoAux.splice(indiceAux, 1);	  
							 }
//							 //Se actualizan los ordenes de los elementos de la lista
							 vm.aumentarOrdenHermanos(entradasMenuDestino);				
							 
						} else {
							console.log("-------- onDropCompleteEntr -----------");
							vm.onReordenarEntr(indice, data, entradasMenuDestino);
						}
						 //Si ya existe significa que hay que reordenar dentro de la lista
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
            });
	        }
	    };
	}
})();