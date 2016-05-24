(function() {
	'use strict';

	angular.module('app').controller('ImportShapefileController', ImportShapefileController);

	ImportShapefileController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile',
	                                'SweetAlert',  'importShapefileService', 'dbconnections', 'dbconcepts'];
	
	function ImportShapefileController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, SweetAlert, importShapefileService,
			dbconnections, dbconcepts) {
	
		var vm = this;
		
		vm.shapefileForm = {};
		
		//parametro del formulario
		/*vm.shapefileForm.dbconnection = null;
		vm.shapefileForm.dbconcept = null;
		vm.shapefileForm.table = null;
		vm.shapefileForm.filezip = null;*/
		
		vm.dbconnections = dbconnections;
		vm.dbconcepts = dbconcepts;
		
		vm.createTable = createTable;
		vm.validarFormulario = validarFormulario;
		vm.validarDbConnection = validarDbConnection;
		vm.validarDbConcept = validarDbConcept;
		vm.recuperarConnection = recuperarConnection;
		vm.recuperarDbConcept = recuperarDbConcept;
		vm.submitForm = submitForm;
				
		function createTable(){
			if (table.checked){
				vm.shapefileForm.dbconcept = undefined;
			}else{
				vm.shapefileForm.tableName = undefined;
			}
			
			//para que se quiten los errores si los hay
			validarDbConcept();
		}
		
		function validarDbConnection(){
			vm.errorConnection = false;
			if (vm.dbconnection === undefined){
				vm.errorConnection = true;
			}
		}
		
		function validarDbConcept(){
			vm.errorConcept = false;
			if (vm.table === undefined){
				//Si es undefined es porque no se selecciono, entonces dbconcept tiene que tener valor
				if (vm.dbconcept === undefined){
					vm.errorConcept = true;
				}
			}else{
				//Si no es  undefined es porque se marco/desmarco
				if (!vm.table){
					//Si no esta seleccionado, dbconcept tiene que tener valor
					if (vm.dbconcept === undefined){
						vm.errorConcept = true;
					}
				}else{
					if (vm.tableName === undefined){
						vm.errorConcept = true;
					}
				}
			}
			
		}
		function validarFormulario(){
			var error = false;
			vm.errorConnection = false;
			vm.errorFilezip = false;
			vm.errorConcept = false;
			
			if (vm.dbconnection === undefined){
				vm.errorConnection = true;
				error = true;
			}
			
			if (vm.filezip === undefined){
				vm.errorFilezip = true;
				error = true;
			}
			
			if (vm.table === undefined){
				//Si es undefined es porque no se selecciono, entonces dbconcept tiene que tener valor
				if (vm.dbconcept === undefined){
					vm.errorConcept = true;
					error = true;
				}
			}else{
				//Si no es  undefined es porque se marco/desmarco
				if (!vm.table){
					//Si no esta seleccionado, dbconcept tiene que tener valor
					if (vm.dbconcept === undefined){
						vm.errorConcept = true;
						error = true;
					}
				}else{
					if (vm.tableName === undefined){
						vm.errorConcept = true;
						error = true;
					}
				}
			}
			
			return error;
			
		}
		
		function recuperarConnection() {
	    	var idConnectionToFind = vm.dbconnection;
	    	for(var i=0; i<vm.dbconnections.length;i++){
	    		var connection = vm.dbconnections[i];
	    		if (idConnectionToFind === connection.id){
	    			return connection;
	    		}
	    	}
	    }
	    
	    function recuperarDbConcept() {
	    	var idDbConceptToFind = vm.dbconcept;
	    	for(var i=0; i<vm.dbconcepts.length;i++){
	    		var dbconcept = vm.dbconcepts[i];
	    		if (idDbConceptToFind === dbconcept.id){
	    			return dbconcept;
	    		}
	    	}
	    }
	    
	    function submitForm() {
	    	vm.infoAction = undefined;
	    	
	    	var error = validarFormulario();
	    	
	    	if (!error){
	    		//Recuperamos el osmConcept/dbConcept asociada al id indicado
	    		var dbConnection = recuperarConnection();

	    		var dbConcept = null;
	    		var dbConceptName = null;
	    		if (vm.table){
	    			dbConceptName = vm.tableName;	 
	    		}else{
	    			dbConcept = vm.recuperarDbConcept();
	    		}

	    		var importacion = {
	    				dbConnection: dbConnection, 
	    				dbConcept: dbConcept,
	    				dbConceptName: dbConceptName,
	    				filezip: vm.filezip};	

	    		importShapefileService.importar(importacion).then(function(response){
	    			vm.typeAction = response.data.type;
					vm.infoAction = response.data.value;
	    		});	
	    	}
	    	
	    }
		
	}
	
})();