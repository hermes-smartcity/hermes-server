(function() {
	'use strict';

	angular.module('app').controller('DBConnectionController', DBConnectionController);

	DBConnectionController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'dbConnectionService', 'SweetAlert',  
	                                'DTOptionsBuilder', 'DTColumnBuilder', 
	                                'dbconnectionstype', 'dbconnections'];

	function DBConnectionController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , dbConnectionService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, dbconnectionstype, dbconnections) {
	
		var vm = this;
		
		vm.dbconnectionstype = dbconnectionstype;
		vm.dbconnections = dbconnections;
		
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
			dfd.resolve(vm.dbconnections);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('dbconnection.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('dbconnection.name')),
		                   DTColumnBuilder.newColumn('type').withTitle($translate.instant('dbconnection.type')),
		                   DTColumnBuilder.newColumn('host').withTitle($translate.instant('dbconnection.host')),
		                   DTColumnBuilder.newColumn('port').withTitle($translate.instant('dbconnection.port')),
		                   DTColumnBuilder.newColumn('dbName').withTitle($translate.instant('dbconnection.dbname')),
		                   DTColumnBuilder.newColumn('userDb').withTitle($translate.instant('dbconnection.userDb')),
		                   //DTColumnBuilder.newColumn('passDb').withTitle($translate.instant('dbconnection.passDb')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('dbconnection.passDb')).renderWith(function(data,type,full) {
		                	   var texto = "************";
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('dbconnection.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                       return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
		                           '   <i class="fa fa-edit"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
		                           '   <i class="fa fa-trash-o"></i>' +
		                           '</button>';
		                   })
		                ];
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/dbconnection/modal-form.html',
                controller: 'DBConnectionModalController',
                scope: $scope,
                resolve: {
                	infoConnection: function(){
                    	return null;
                    },
                	types: function () {
                        return vm.dbconnectionstype;
                    },
                    connectionForm: function () {
                        return $scope.connectionForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	dbConnectionService.getDbConnections().then(function(response) {
		     		vm.dbconnections = response;
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
                templateUrl: './partials/dbconnection/modal-form.html',
                controller: 'DBConnectionModalController',
                scope: $scope,
                resolve: {
                	infoConnection: function(){
                    	return dbConnectionService.getDBConnection(id);
                    },
                	types: function () {
                        return vm.dbconnectionstype;
                    },
                    connectionForm: function () {
                        return $scope.connectionForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
	        	vm.infoAction = response;
                
	        	dbConnectionService.getDbConnections().then(function(response) {
		     		vm.dbconnections = response;
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
	        			dbConnectionService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		dbConnectionService.getDbConnections().then(function(response) {
		   	        			vm.dbconnections = response;
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