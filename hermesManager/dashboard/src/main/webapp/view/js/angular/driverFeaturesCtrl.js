var driverFeaturesApp = angular.module('driverFeaturesApp', ['ngRoute']);
var driverFeaturesApp = angular.module('driverFeaturesApp', []);


driverFeaturesApp.controller('DriversFeaturesController', [ '$scope', '$http',
                                     
	function($scope, $http) {
		$scope.getDriversFeatures = function() {
			var idUsuario = getUrlParameter("idUsuario");
			var urlGet = "json/driversFeatures";
			if(idUsuario != null)
				urlGet = "json/driversFeaturesByUsuario?"+"idUsuario="+idUsuario;
		}
		
	} ]);