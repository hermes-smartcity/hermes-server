(function() {
	'use strict';

	angular.module('app').controller('FormularioController', FormularioController);

	FormularioController.$inject = ['$state', '$q', 'menuService', '$scope' ];

	function FormularioController($state, $q, menuService, $scope) {
		var vm = this;
		vm.entradasMenu = [];
		
		//TODO cambiar a que valide correctamente!
		vm.nombre = '';
//		vm.menu = {};
		vm.addNuevoMenuEntrada = addNuevoMenuEntrada;
		vm.removeChoice = removeChoice;
		vm.validarDatos = validarDatos;
		vm.enviar = enviar;
		
		vm.onSoltarCompletado = onSoltarCompletado;
		vm.onArrastrarCompletado = onArrastrarCompletado;
		vm.onReordenarEntr = onReordenarEntr;		
		
		vm.arrastradoAnteriormente = false;
		vm.arrayDestinoAux = [];
		
		// Entradas Menus		
		function addNuevoMenuEntrada() {
			var newItemNo = vm.entradasMenu.length+1;
			// "Chapucilla momentánea" falta decidir como se va a hacer
			vm.entradasMenu.push({'orden':newItemNo,'entradasMenu':[],'identacion':50});
		}

		function removeChoice() {
			var lastItem = vm.entradasMenu.length-1;
			vm.entradasMenu.splice(lastItem);
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
//				 //Se actualizan los ordenes de los elementos de la lista
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

		function enviar() {
			// Se llama a la funcion menu servicio y se guarda el menu
			var menu = {'nombre':vm.nombre,'entradasMenu':vm.entradasMenu};
			menuService.guardarMenu(menu);
			$state.go('inicio');
		}

		// Nombre del menu obligatorio 
		function validarDatos() {
			if (angular.isUndefinedOrNull(vm.nombre))
				return false;
			return true;
		}
			}
})();