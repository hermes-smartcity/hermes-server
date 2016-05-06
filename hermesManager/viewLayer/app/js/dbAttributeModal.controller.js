(function() {
	'use strict';

	angular.module('app').controller('DBAttributeModalController', DBAttributeModalController);

	DBAttributeModalController.$inject = ['$scope', '$uibModalInstance', 'attributeForm', 
	                                       'infoAttribute', 'idConcept', 'attributestypes',
	                                       'dbAttributeService'];

	function DBAttributeModalController($scope, $uibModalInstance, attributeForm, infoAttribute, 
			idConcept, attributestypes, dbAttributeService) {
	
		$scope.form = {};
		
		$scope.idConcept = idConcept;
		$scope.attributestypes = attributestypes;
		
		//Si infoConnection no es undefined, es porque estamos editando
		if (infoAttribute !== null){
			$scope.name = infoAttribute.data.attributeName;
			$scope.type = infoAttribute.data.attributeType;
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.attributeForm.$valid) {
	            
	            if (infoAttribute === null){
	            	//es un alta
	            	var atributoNuevo = {id: null,
	            		   attributeName: $scope.form.attributeForm.name.$viewValue,
	            		   attributeType: $scope.form.attributeForm.type.$viewValue, 
	            		   dbConcept: $scope.idConcept};	
     
	            	dbAttributeService.register(atributoNuevo).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var atributoEditar = {id: infoAttribute.data.id, 
	            			attributeName: $scope.form.attributeForm.name.$viewValue,
		            		attributeType: $scope.form.attributeForm.type.$viewValue, 
		            		dbConcept: $scope.idConcept};	
	        		
	            	dbAttributeService.edit(atributoEditar).then(function(response){
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
	}
})();