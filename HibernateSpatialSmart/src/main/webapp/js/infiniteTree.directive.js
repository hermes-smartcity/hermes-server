(function() {
	'use strict';

	angular.module('app').directive('entradaMenuDirect', entradaMenuDirect);

	entradaMenuDirect.$inject = ['$compile'];

	function entradaMenuDirect($compile) {
		return {
			templateUrl: 'partials/entradaMenuTree.htm',
			 restrict: 'E',
			    replace: true,
			    require: '^entradaMenuDirect',
			    scope: {
			      // Variable de este scope que utilizo en la directiva. Lado derecho el atributo desde donde llama a la directiva, el nombre que debe tener 	
			    	entradaMenuParam: '=entradaMenuParam'
			    },			    
			    link: function($scope, $element, controller) {
			    	 console.log("entro por aki????'");
			    	this.entradasMenu = $scope.entradasMenu;
			    	this.addNuevoMenuEntrada =  $scope.addNuevoMenuEntrada;
			    	this.removeChoice =  $scope.removeChoice;
			    	this.validarDatos =  $scope.validarDatos;
					
			    	this.addNuevoMenuEntradaHija =  addNuevoMenuEntradaHija;
			    	this.addNuevoMenuEntradaHermanoArriba =  addNuevoMenuEntradaHermanoArriba;
			    	this.addNuevoMenuEntradaHermanoAbajo =  addNuevoMenuEntradaHermanoAbajo;
			
			    	this.aumentarOrdenHermanos =  aumentarOrdenHermanos;
										
			    	this.arrastradoAnteriormente = false;
			    	this.arrayDestinoAux = [];
			    	
			    	function addNuevoMenuEntradaHermanoArriba(entradaMenu, entradaMenuPadre, indice) {		
						console.log("---addNuevoMenuEntradaHermanoArriba "+addNuevoMenuEntradaHermanoArriba);
						var entradasMenuDestino = [];
						var identacion;
						if(entradaMenuPadre == null){				
							entradasMenuDestino = this.entradasMenu;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						entradasMenuDestino.splice(indice,0,{'orden':indice,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						this.aumentarOrdenHermanos(entradasMenuDestino);
					};
					
					function addNuevoMenuEntradaHermanoAbajo(entradaMenu, entradaMenuPadre, indice) {		
						var entradasMenuDestino = [];
						var identacion;
						//TODO hacer funcion
						if(entradaMenuPadre == null){				
							entradasMenuDestino = this.entradasMenu;
							identacion = 50;
						} else {
							entradasMenuDestino = entradaMenuPadre.entradasMenu;
							identacion = entradaMenuPadre.identacion + 50; // Va a ser hija de su menu padre, por lo tanto mas identacion
						}
						
						var i = indice+1;
						entradasMenuDestino.splice(i,0,{'orden':i,'entradasMenu':[], 'identacion':identacion});
						// Todas las entradas que le siguen abajo deben aumentar el orden
						this.aumentarOrdenHermanos(entradasMenuDestino);
			  
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
			        $element.append('<entradaMenuDirect ng-repeat="entradasMenu in entradaMenu.entradasMenu" entradaMenuParam="entradasMenu"></entradaMenuDirect>');
			        $compile($element.contents().last())($scope);
			      }
			    }
		}
	}
})();