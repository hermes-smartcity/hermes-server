(function() {
	'use strict';

	angular.module('app').controller('AttributeMappingModalController', AttributeMappingModalController);

	AttributeMappingModalController.$inject = ['$scope', '$uibModalInstance', 'attributeForm', 
	                                       'infoAttribute', 'idConceptTransformation', 'osmattributes', 
	                                       'dbattributes', 'attributeMappingService'];

	function AttributeMappingModalController($scope, $uibModalInstance, attributeForm, infoAttribute, 
			idConceptTransformation, osmattributes, dbattributes, attributeMappingService) {
	
		$scope.form = {};
		
		$scope.idConceptTransformation = idConceptTransformation;
		$scope.osmattributes = osmattributes.data;
		$scope.dbattributes = dbattributes.data;
				
		//Si infoAttribute no es undefined, es porque estamos editando
		if (infoAttribute !== null){
			$scope.dbattribute = infoAttribute.data.dbAttribute.id;
			$scope.osmattribute = infoAttribute.data.osmAttribute.id;
		}
		
		
		$scope.submitForm = function () {
	        if ($scope.form.attributeForm.$valid) {
	            
	        	//Recuperamos el osmAttribute/dbAttribute asociada al id indicado
	        	var osmAttribute = $scope.recuperarOsmAttribute();
	        	var dbAttribute = $scope.recuperarDbAttribute();
	        		        	
	            if (infoAttribute === null){
	            	//es un alta
	            	var attributeMappingNueva = {id: null,
	            			conceptTransformation: $scope.idConceptTransformation,
	            			dbAttribute: dbAttribute, 
	            			osmAttribute: osmAttribute};	
     
	            	attributeMappingService.register(attributeMappingNueva).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var attributeMappingEditar = {id: infoAttribute.data.id, 
	            			conceptTransformation: $scope.idConceptTransformation,
	            			dbAttribute: dbAttribute, 
	            			osmAttribute: osmAttribute};	
	        		
	            	attributeMappingService.edit(attributeMappingEditar).then(function(response){
				        $uibModalInstance.close(response.data);
				     });
	        		
	            }
	            
	        } else {
	            console.log('attributeForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	    
	    $scope.recuperarOsmAttribute = function () {
	    	var idOsmAttributetToFind = $scope.form.attributeForm.osmattribute.$viewValue;
	    	for(var i=0; i<$scope.osmattributes.length;i++){
	    		var osmattribute = $scope.osmattributes[i];
	    		if (idOsmAttributetToFind === osmattribute.id){
	    			return osmattribute;
	    		}
	    	}
	    };
	    
	    $scope.recuperarDbAttribute = function () {
	    	var idDbAttributetToFind = $scope.form.attributeForm.dbattribute.$viewValue;
	    	for(var i=0; i<$scope.dbattributes.length;i++){
	    		var dbattribute = $scope.dbattributes[i];
	    		if (idDbAttributetToFind === dbattribute.id){
	    			return dbattribute;
	    		}
	    	}
	    };
	    
	  
	}
})();