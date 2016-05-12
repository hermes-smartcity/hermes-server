(function() {
	'use strict';

	angular.module('app').controller('OSMConceptModalController', OSMConceptModalController);

	OSMConceptModalController.$inject = ['$scope', '$uibModalInstance', 'conceptForm', 
	                                       'infoConcept', 'osmConceptService'];

	function OSMConceptModalController($scope, $uibModalInstance, conceptForm, infoConcept, 
			osmConceptService) {
	
		$scope.form = {};
		
		
		//Si infoConcept no es undefined, es porque estamos editando
		if (infoConcept !== null){
			$scope.name = infoConcept.data.name;
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.conceptForm.$valid) {
	            
	            if (infoConcept === null){
	            	//es un alta
	            	var conceptNueva = {id: null,
	            			name: $scope.form.conceptForm.name.$viewValue};	
     
	            	osmConceptService.register(conceptNueva).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var conceptEditar = {id: infoConcept.data.id, 
	            			name: $scope.form.conceptForm.name.$viewValue};	
	        		
	            	osmConceptService.edit(conceptEditar).then(function(response){
				        $uibModalInstance.close(response.data);
				     });
	        		
	            }
	            
	        } else {
	            console.log('conceptForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	    
	  
	}
})();