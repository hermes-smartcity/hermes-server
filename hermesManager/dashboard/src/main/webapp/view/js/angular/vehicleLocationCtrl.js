var vehicleLocationApp = angular.module('vehicleLocationApp', ['ngRoute']);
var vehicleLocationApp = angular.module('vehicleLocationApp', []);


vehicleLocationApp.controller('VehicleLocationsController', [ '$scope', '$http',
                                     
	function($scope, $http) {
		$scope.getVehicleLocations = function() {
			var idUsuario = getUrlParameter("idUsuario");
			var urlGet = "json/vehicleLocations";
			if(idUsuario != null)
				urlGet = "json/vehicleLocationsByUsuario?"+"idUsuario="+idUsuario;
			$http.get(urlGet).success(function(data) {
				$scope.vehicleLocations = data;
				
				var map = L.map('map').setView([51.505, -0.09], 13);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
				
				//Este es sincrono espera a q se cargue uno para que cargue el otro
//				$scope.vehicleLocations.forEach(function(value) {
//					console.log(' --------- '+value.position);
//					L.marker(value).addTo(map).bindPopup('A pretty CSS3 popup.Easily customizable.').openPopup();
//				});
				
				var i = 0;
//				//Este es asincrono, se cargan a la vez, no espera q se cargue el anterior
				//Recorro el array geojson de cada uno de los vehicleLocations
				angular.forEach($scope.vehicleLocations, function(value, key) {
					//Parseo la fecha
					var timestamp = value.timestamp;
					var date = new Date(timestamp);
					var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
					$scope.bdatetime = datevalues;
					//Convierto el punto que quiero pintar para tener su lat y log
					var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
					//Añado al mapa el punto
					L.marker(latlng).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime).openPopup();
				});
			});
		}
		
	} ]);