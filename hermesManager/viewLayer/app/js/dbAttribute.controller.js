(function() {
	'use strict';

	angular.module('app').controller('DBAttributeController', DBAttributeController);

	DBAttributeController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'dbAttributeService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idConcept', 'dbattributestype', 'dbattributes'];
	
	function DBAttributeController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , dbAttributeService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idConcept, dbattributestype, dbattributes) {
	
		var vm = this;
		
		vm.idConcept = parseInt(idConcept);
		vm.dbattributestype = dbattributestype;
		vm.dbattributes = dbattributes.data;
		
		vm.add = add;
		vm.edit = edit;
		vm.delet = delet;
		vm.doTheBack = doTheBack;
	
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.dbattributes);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('dbattribute.id')),
		                   DTColumnBuilder.newColumn('attributeName').withTitle($translate.instant('dbattribute.name')),
		                   DTColumnBuilder.newColumn('attributeType').withTitle($translate.instant('dbattribute.type')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('dbattribute.actions')).notSortable()
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
                templateUrl: './partials/dbattribute/modal-form.html',
                controller: 'DBAttributeModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                    	return null;
                    },
                    idConcept: function(){
                    	return vm.idConcept;
                    },
                    attributestypes: function () {
                        return vm.dbattributestype;
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	dbAttributeService.getDbAttributes(vm.idConcept).then(function(response) {
		     		vm.dbattributes = response.data;
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
                templateUrl: './partials/dbattribute/modal-form.html',
                controller: 'DBAttributeModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                		return dbAttributeService.getDbAttribute(id);
                    },
                    idConcept: function(){
                    	return vm.idConcept;
                    },
                    attributestypes: function () {
                        return vm.dbattributestype;
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	dbAttributeService.getDbAttributes(vm.idConcept).then(function(response) {
		     		vm.dbattributes = response.data;
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
	        			dbAttributeService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		dbAttributeService.getDbAttributes(vm.idConcept).then(function(response) {
		   	        			vm.dbattributes = response.data;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
		
		function doTheBack(){
			window.history.back();
		}
	}
	
})();