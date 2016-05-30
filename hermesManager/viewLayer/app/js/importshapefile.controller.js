(function() {
	'use strict';

	angular.module('app').controller('ImportShapefileController', ImportShapefileController);

	ImportShapefileController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile',
	                                'SweetAlert',  'importShapefileService', 
	                                'dbconnections', 'dbconcepts', 'charsets', 'Upload'];
	
	function ImportShapefileController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, SweetAlert, importShapefileService,
			dbconnections, dbconcepts, charsets, Upload) {
	
		var vm = this;
		
		vm.shapefileForm = {};
	
		vm.dbconnections = dbconnections;
		vm.dbconcepts = dbconcepts;
		vm.charsets = charsets;
		
		vm.createTable = createTable;
		vm.validarFormulario = validarFormulario;
		vm.validarDbConnection = validarDbConnection;
		vm.validarDbConcept = validarDbConcept;
		vm.validarCharset = validarCharset;
		vm.validarFile = validarFile;
		vm.recuperarConnection = recuperarConnection;
		vm.recuperarDbConcept = recuperarDbConcept;
		
		vm.uploadFiles = uploadFiles;
		vm.submitForm = submitForm;
				
		function createTable(){
			if (table.checked){
				vm.shapefileForm.dbconcept = undefined;
				vm.dbconcept = undefined;
			}else{
				vm.shapefileForm.tableName = undefined;
				vm.shapefileForm.schemaName = undefined;
				vm.tableName = undefined;
				vm.schemaName = undefined;
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
					
					if (vm.schemaName === undefined){
						vm.errorConcept = true;
					}
				}
			}
			
		}
		
		function validarCharset(){
			vm.errorCharset = false;
			if (vm.charset === undefined){
				vm.errorCharset = true;
			}
		}
		
		function validarFile(){
			vm.errorFilezip = false;
			if (vm.filezip === undefined){
				vm.errorFilezip = true;
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
					
					if (vm.schemaName === undefined){
						vm.errorConcept = true;
						error = true;
					}
				}
			}
			
			if (vm.charset === undefined){
				vm.errorCharset = true;
				error = true;
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
	    
	    function uploadFiles(file){
	    	vm.filezip = file;
	    }
	    
	    function submitForm() {
	    	vm.infoAction = undefined;
	    	
	    	var error = validarFormulario();
	    	
	    	if (!error){
	    		//Recuperamos el osmConcept/dbConcept asociada al id indicado
	    		var dbConnection = recuperarConnection();

	    		var dbConcept = null;
	    		var dbConceptName = null;
	    		var dbConceptSchema = null;
	    		if (vm.table){
	    			dbConceptName = vm.tableName;	
	    			dbConceptSchema = vm.schemaName;
	    		}else{
	    			dbConcept = vm.recuperarDbConcept();
	    		}

	    		var importacion = {
	    				dbConnection: vm.dbconnection, 
	    				dbConcept: vm.dbconcept,
	    				dbConceptName: dbConceptName,
	    				dbConceptSchema: dbConceptSchema,
	    				keepExistingData: vm.keep, 
	    				charset: vm.charset};	

	    		importShapefileService.importar(importacion, vm.filezip).then(function(response){
	    			vm.typeAction = response.data.type;
					vm.infoAction = response.data.value;
	    		});	
	    	}
	    	
	    }
		
	}
	
})();