(function() {
	'use strict';

	angular.module('app').controller('OSMAttributeModalController', OSMAttributeModalController);

	OSMAttributeModalController.$inject = ['$scope', '$uibModalInstance', 'attributeForm', 
	                                       'infoAttribute', 'idOsmConcept', 
	                                       'osmAttributeService'];

	function OSMAttributeModalController($scope, $uibModalInstance, attributeForm, infoAttribute, 
			idOsmConcept, osmAttributeService) {
	
		$scope.form = {};
		
		$scope.idOsmConcept = idOsmConcept;
				
		//Si infoConnection no es undefined, es porque estamos editando
		if (infoAttribute !== null){
			$scope.name = infoAttribute.data.name;
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.attributeForm.$valid) {
	            
	            if (infoAttribute === null){
	            	//es un alta
	            	var atributoNuevo = {id: null,
	            		   name: $scope.form.attributeForm.name.$viewValue, 
	            		   osmConcept: $scope.idOsmConcept};	
     
	            	osmAttributeService.register(atributoNuevo).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var atributoEditar = {id: infoAttribute.data.id, 
	            			name: $scope.form.attributeForm.name.$viewValue, 
		            		osmConcept: $scope.idOsmConcept};	
	        		
	            	osmAttributeService.edit(atributoEditar).then(function(response){
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