(function() {
	'use strict';

	angular.module('app').directive('entradaMenuTree', entradaMenuTree);

	audicion.$inject = ['$sce'];

	function entradaMenuTree($sce) {
		return {
			templateUrl: 'partials/entradaMenuTree.htm',
			
			}
		}
	}
})();