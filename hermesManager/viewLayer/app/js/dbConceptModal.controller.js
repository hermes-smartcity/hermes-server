(function() {
	'use strict';

	angular.module('app').controller('DBConceptModalController', DBConceptModalController);

	DBConceptModalController.$inject = ['$scope', '$uibModalInstance', 'conceptForm', 
	                                       'infoConcept', 'dbconnections', 'dbConceptService'];

	function DBConceptModalController($scope, $uibModalInstance, conceptForm, infoConcept, 
			dbconnections, dbConceptService) {
	
		$scope.form = {};
		
		$scope.dbconnections = dbconnections;
		
		//Si infoConcept no es undefined, es porque estamos editando
		if (infoConcept !== null){
			$scope.name = infoConcept.data.name;
			$scope.schemaName = infoConcept.data.schemaName;
			$scope.tableName = infoConcept.data.tableName;
			$scope.osmIdName = infoConcept.data.osmIdName;
			$scope.geomName = infoConcept.data.geomName;
			$scope.dbconnection = infoConcept.data.dbConnection.id;

		}
		
		$scope.submitForm = function () {
	        if ($scope.form.conceptForm.$valid) {
	            
	        	//Recuperamos el dbConnection asociada al id indicado
	        	var connection = $scope.recuperarConnection();
	            if (infoConcept === null){
	            	//es un alta
	            	var conceptNueva = {id: null,
	            			name: $scope.form.conceptForm.name.$viewValue,
	            			schemaName: $scope.form.conceptForm.schemaName.$viewValue, 
	            			tableName: $scope.form.conceptForm.tableName.$viewValue,  
	            			osmIdName: $scope.form.conceptForm.osmIdName.$viewValue,  
	            			geomName: $scope.form.conceptForm.geomName.$viewValue,  
	            			dbConnection: connection};	
     
	            	dbConceptService.register(conceptNueva).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var conceptEditar = {id: infoConcept.data.id, 
	            			name: $scope.form.conceptForm.name.$viewValue,
	            			schemaName: $scope.form.conceptForm.schemaName.$viewValue, 
	            			tableName: $scope.form.conceptForm.tableName.$viewValue, 
	            			osmIdName: $scope.form.conceptForm.osmIdName.$viewValue,  
	            			geomName: $scope.form.conceptForm.geomName.$viewValue,
	            			dbConnection: connection};	
	        		
	            	dbConceptService.edit(conceptEditar).then(function(response){
				        $uibModalInstance.close(response.data);
				     });
	        		
	            }
	            
	        } else {
	            console.log('connectionForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	    
	    $scope.recuperarConnection = function () {
	    	var idConnectionToFind = $scope.form.conceptForm.dbconnection.$viewValue;
	    	for(var i=0; i<$scope.dbconnections.length;i++){
	    		var connection = $scope.dbconnections[i];
	    		if (idConnectionToFind === connection.id){
	    			return connection;
	    		}
	    	}
	    };
	}
})();