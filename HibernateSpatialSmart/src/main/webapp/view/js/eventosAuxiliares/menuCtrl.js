var menuApp = angular.module('menuApp', ['ngRoute']);
var menuApp = angular.module('menuApp', []);
var menuApp = angular.module('menuApp', ['ngDraggable']);

(function() {
	'use strict';

angular.module('menuApp').controller('MenusController', MenusController);

MenusController.$inject = ['$scope', '$http' , 'menuService', '$q'];

function MenusController($scope, $http , menuService, $q) {
		// menu declarado, que contendrá nombre de atributo, y entradas menu que habrá que anadir
		$scope.nombre = '';
				
		//inicializacion prueba
//		$scope.entradasMenu = [
//				              {'texto':'textoMenu','url':'url','orden':'1',
//				            	  'entradasMenu':[{	'texto':'textoMenu','url':'url','orden':'1',
//				            		  'entradasMenu':[{	'texto':'textoMenu**','url':'url','orden':'1'}]}]},
//				              {'texto':'textoMenu2','url':'url2','orden':'2',
//				            		  'entradasMenu':[]}];
	
			 
		var idMenu = getUrlParameter("idMenu");
		if(idMenu!=null){
			urlGet = "json/entradasMenu?"+"idMenu="+idMenu;
			$scope.entradasMenu = $http.get(urlGet).success(function(data) {
				$scope.entradasMenu = data;
			});
		} else $scope.entradasMenu = [];
			
		
			// Entradas Menus		
		   $scope.addNuevoMenuEntrada = function() {
		    var newItemNo = $scope.entradasMenu.length+1;
		    $scope.entradasMenu.push({'orden':newItemNo,'entradasMenu':[]});

		    var menu = {'nombre':$scope.nombre,'entradasMenu':$scope.entradasMenu};
			menuService.guardarMenu(menu);

		  };
	    
		  $scope.removeChoice = function() {
		    var lastItem = $scope.entradasMenu.length-1;
		    $scope.entradasMenu.splice(lastItem);
		  }; 
		  
		  //Entradas hijas
		// Entradas Menus		
		   $scope.addNuevoMenuEntradaHija = function(entradaMenu) {
			   	
			   var newItemNo = entradaMenu.entradasMenu.length+1;
			   console.log(" --- newItemNo --- "+entradaMenu.texto);
			   entradaMenu.entradasMenu.push({'orden':newItemNo,'entradasMenu':[]});
			   console.log('-- --- -- -- - -- '+entradaMenu.entradasMenu[newItemNo-1].orden+'-- --- -- -- - -- '+entradaMenu.texto);
		  };
		  
		   $scope.addHijo = function(index) {
			    var newItemNo = $scope.entradasMenu[index].entradasMenu.length+1;
			    $scope.entradasMenu[index].entradasMenu.push({'orden':newItemNo,'entradasMenu':[]});

			};
			  
		  // Reordenar entradas menu
		  $scope.onDropCompleteEntr = function (index, obj, evt) {
	           var otherObj = $scope.entradasMenu[index];
	           var otherIndex = $scope.entradasMenu.indexOf(obj);
	           $scope.entradasMenu[index] = obj;
	           $scope.entradasMenu[index].orden = index;
	           $scope.entradasMenu[otherIndex] = otherObj;
	           $scope.entradasMenu[otherIndex].orden = otherIndex;
	       };
	       
	       
	       
	      		  
}
})();