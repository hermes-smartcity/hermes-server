(function() {
	'use strict';

	angular.module('app').controller('OSMFilterModalController', OSMFilterModalController);

	OSMFilterModalController.$inject = ['$scope', '$uibModalInstance', 'filterForm', 
	                                       'infoFilter', 'idOsmConcept', 'operations',
	                                       'osmFilterService'];

	function OSMFilterModalController($scope, $uibModalInstance, filterForm, infoFilter, 
			idOsmConcept, operations, osmFilterService) {
	
		$scope.form = {};
		
		$scope.idOsmConcept = idOsmConcept;
		$scope.operations = operations;
		
		//Si infoConnection no es undefined, es porque estamos editando
		if (infoFilter !== null){
			$scope.name = infoFilter.data.name;
			$scope.type = infoFilter.data.operation;
			$scope.value = infoFilter.data.value;
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.filterForm.$valid) {
	            
	            if (infoFilter === null){
	            	//es un alta
	            	var filterNuevo = {id: null,
	            		  name: $scope.form.filterForm.name.$viewValue,
	            		   operation: $scope.form.filterForm.operation.$viewValue, 
	            		   value: $scope.form.filterForm.value.$viewValue,
	            		   osmConcept: $scope.idOsmConcept};	
     
	            	osmFilterService.register(filterNuevo).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var filterEditar = {id: infoFilter.data.id, 
	            			name: $scope.form.filterForm.name.$viewValue,
		            		operation: $scope.form.filterForm.operation.$viewValue,
		            		value: $scope.form.filterForm.value.$viewValue,
		            		osmConcept: $scope.idOsmConcept};	
	        		
	            	osmFilterService.edit(filterEditar).then(function(response){
				        $uibModalInstance.close(response.data);
				     });
	        		
	            }
	            
	        } else {
	            console.log('filterForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	}
})();