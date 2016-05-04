(function() {
	'use strict';

	angular.module('app').controller('DBConnectionController', DBConnectionController);

	DBConnectionController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', 
	                                'dbConnectionService', 'SweetAlert',  
	                                'DTOptionsBuilder', 'DTColumnBuilder', 
	                                'dbconnectionstype', 'dbconnections', ];

	function DBConnectionController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, dbConnectionService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, dbconnectionstype, dbconnections) {
	
		var vm = this;
		
		vm.dbconnectionstype = dbconnectionstype;
		vm.dbconnections = dbconnections;
		
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
		                   DTColumnBuilder.newColumn('type').withTitle($translate.instant('dbconnection.type')),
		                   DTColumnBuilder.newColumn('host').withTitle($translate.instant('dbconnection.host')),
		                   DTColumnBuilder.newColumn('port').withTitle($translate.instant('dbconnection.port')),
		                   DTColumnBuilder.newColumn('dbName').withTitle($translate.instant('dbconnection.dbname')),
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
		
		function edit (id) {
	        console.log('Editing ' + id);
	        // Edit some data and call server to make changes...
	        // Then reload the data so that DT is refreshed
	        vm.dtOptions.reloadData();
	    }
	    
	    function delet(id) {
	        console.log('Deleting' + id);
	        
	        SweetAlert.swal({
	        	   title: $translate.instant('confirmDelete'),
	        	   text: $translate.instant('textDelete'),
	        	   type: "warning",
	        	   showCancelButton: true,
	        	   confirmButtonColor: "#DD6B55",
	        	   confirmButtonText: $translate.instant('aceptar'),
	        	   closeOnConfirm: false,
	        	   closeOnCancel: true 
	        	}, 
	        	function(isConfirm){
	        		if (isConfirm){
	        			dbConnectionService.delet(id).then(function() {
		   	        		vm.infoAction = response.data;
		   	        		dbConnectionService.getDbConnections().then(function() {
		   	        			vm.dbconnections = response.data;
		   						 vm.dtOptions.reloadData();
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
	}
})();