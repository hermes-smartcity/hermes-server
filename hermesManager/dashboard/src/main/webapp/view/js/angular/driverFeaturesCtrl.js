var driverFeaturesApp = angular.module('driverFeaturesApp', ['ngRoute']);
var driverFeaturesApp = angular.module('driverFeaturesApp', []);


driverFeaturesApp.controller('DriversFeaturesController', [ '$scope', '$http',
                                     
	function($scope, $http) {
		$scope.getDriversFeatures = function() {
			var idUsuario = getUrlParameter("idUsuario");
			var urlGet = "json/driversFeatures";
			if(idUsuario != null)
				urlGet = "json/driversFeaturesByUsuario?"+"idUsuario="+idUsuario;
			$http.get(urlGet).success(function(data) {
				$scope.driversFeatures = data;
				
				var map = L.map('map').setView([51.505, -0.09], 13);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
				
				var i = 0;
//				//Este es asincrono, se cargan a la vez, no espera q se cargue el anterior
				//Recorro el array geojson de cada uno de los driversFeatures
				angular.forEach($scope.driversFeatures, function(value, key) {
					//Parseo la fecha
					var timestamp = value.timestamp;
					var date = new Date(timestamp);
					var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
					$scope.bdatetime = datevalues;
					//Convierto el punto que quiero pintar para tener su lat y log
					var latlng = L.latLng(value.position.coordinates);
					//Añado al mapa el punto
					L.marker(latlng).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime).openPopup();
				});
			});
		}
		
	} ]);