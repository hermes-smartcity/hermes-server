(function() {
	'use strict';

	angular.module('app').controller('DBConceptController', DBConceptController);

	DBConceptController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'dbConceptService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'dbconcepts'];
	
	function DBConceptController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , dbConceptService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, dbconcepts) {
	
		var vm = this;
		
		vm.dbconcepts = dbconcepts;
		
		vm.add = add;
		vm.edit = edit;
		vm.delet = delet;
	
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.dbconcepts);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('dbconcept.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('dbconcept.name')),
		                   DTColumnBuilder.newColumn('schemaName').withTitle($translate.instant('dbconcept.schemaName')),
		                   DTColumnBuilder.newColumn('tableName').withTitle($translate.instant('dbconcept.tableName')),
		                   DTColumnBuilder.newColumn('idName').withTitle($translate.instant('dbconcept.idName')),
		                   DTColumnBuilder.newColumn('osmIdName').withTitle($translate.instant('dbconcept.osmIdName')),
		                   DTColumnBuilder.newColumn('geomName').withTitle($translate.instant('dbconcept.geomName')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('dbconcept.dbconnection')).renderWith(function(data,type,full) {
		                	   var texto = data.dbConnection.name;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('dbconcept.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                       return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
		                           '   <i class="fa fa-edit"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
		                           '   <i class="fa fa-trash-o"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-info" data-ui-sref="manageAttributes({idConcept:'+ data.id + '})" >' +
		                           	$translate.instant('dbconcept.manageAttributes') + 
		                           '</button>';
		                   })
		                ];  
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/dbconcept/modal-form.html',
                controller: 'DBConceptModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                    	return null;
                    },
                	dbconnections: function () {
                		return dbConceptService.getDbConnections();
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	dbConceptService.getDbConcepts().then(function(response) {
		     		vm.dbconcepts = response;
	        		if (vm.dtInstance !== null){
	        			vm.dtInstance.reloadData();
	        		}
	        	});
    	        
            }, function () {
            	console.log('Modal dismissed at: ' + new Date());
            });
		}
		
		function edit (id) {
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/dbconcept/modal-form.html',
                controller: 'DBConceptModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                		return dbConceptService.getDbConcept(id);
                    },
                	dbconnections: function () {
                		return dbConceptService.getDbConnections();
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	dbConceptService.getDbConcepts().then(function(response) {
		     		vm.dbconcepts = response;
	        		if (vm.dtInstance !== null){
	        			vm.dtInstance.reloadData();
	        		}
	        	});
    	        
            }, function () {
            	console.log('Modal dismissed at: ' + new Date());
            });
	    }

		function delet(id) {
	        
	    	vm.infoAction = undefined;
	    	
	        SweetAlert.swal({
	        	   title: $translate.instant('confirmDelete'),
	        	   text: $translate.instant('textDelete'),
	        	   type: "warning",
	        	   showCancelButton: true,
	        	   confirmButtonColor: "#DD6B55",
	        	   confirmButtonText: $translate.instant('aceptar'),
	        	   closeOnConfirm: true,
	        	   closeOnCancel: true 
	        	}, 
	        	function(isConfirm){
	        		if (isConfirm){
	        			dbConceptService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		dbConceptService.getDbConcepts().then(function(response) {
		   	        			vm.dbconcepts = response;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
	}
	
})();