(function() {
	'use strict';

	angular.module('app').controller('DBConnectionModalController', DBConnectionModalController);

	DBConnectionModalController.$inject = ['$scope', '$uibModalInstance', 'connectionForm', 
	                                       'infoConnection', 'types', 'dbConnectionService',
	                                       '$translate'];

	function DBConnectionModalController($scope, $uibModalInstance, connectionForm, infoConnection, types, 
			dbConnectionService, $translate) {
	
		$scope.form = {};
		
		$scope.types = types;
		
		//Si infoConnection no es undefined, es porque estamos editando
		if (infoConnection !== null){
			$scope.name = infoConnection.data.name;
			$scope.type = infoConnection.data.type;
			$scope.host = infoConnection.data.host;
			$scope.port = infoConnection.data.port;
			$scope.dbName = infoConnection.data.dbName;
			$scope.userDb = infoConnection.data.userDb;
			$scope.passDb = infoConnection.data.passDb;
			
			//Titulo
			$scope.tituloPagina = $translate.instant('dbconnection.edit');
		}else{
			//Titulo
			$scope.tituloPagina = $translate.instant('dbconnection.create');
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.connectionForm.$valid) {
	            
	            if (infoConnection === null){
	            	//es un alta
	            	var connectionNueva = {id: null,
	            		   name: $scope.form.connectionForm.name.$viewValue,
	            		   type: $scope.form.connectionForm.type.$viewValue, 
     					   host: $scope.form.connectionForm.host.$viewValue, 
     					   port: parseInt($scope.form.connectionForm.port.$viewValue), 
     					   dbName: $scope.form.connectionForm.dbName.$viewValue,
     					   userDb: $scope.form.connectionForm.userDb.$viewValue,
     					   passDb: $scope.form.connectionForm.passDb.$viewValue};	
     
				     dbConnectionService.register(connectionNueva).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var connectionEditar = {id: infoConnection.data.id, 
	            			name: $scope.form.connectionForm.name.$viewValue,
	            			type: $scope.form.connectionForm.type.$viewValue, 
	     					host: $scope.form.connectionForm.host.$viewValue, 
	     					port: parseInt($scope.form.connectionForm.port.$viewValue), 
	     					dbName: $scope.form.connectionForm.dbName.$viewValue,
	     					userDb: $scope.form.connectionForm.userDb.$viewValue,
	     					passDb: $scope.form.connectionForm.passDb.$viewValue};	
	        		
	            	dbConnectionService.edit(connectionEditar).then(function(response){
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
	}
})();