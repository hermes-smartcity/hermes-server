var measurementApp = angular.module('measurementApp', ['ngRoute']);
var measurementApp = angular.module('measurementApp', []);


measurementApp.controller('MeasurementsController', [ '$scope', '$http',
                                     
	function($scope, $http) {
		$scope.getMeasurements = function() {
			var tipoMeasurement = getUrlParameter("tipo");
			var idUsuario = getUrlParameter("idUsuario");
			var urlGet = "json/measurements?tipo="+tipoMeasurement;
			if(idUsuario != null)
				urlGet = "json/measurementsByUsuario?tipo="+tipoMeasurement+"&idUsuario="+idUsuario;
				
		
			$http.get(urlGet).success(function(data) {
				$scope.measurements = data;
				
				var map = L.map('map').setView([51.505, -0.09], 13);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
			
				angular.forEach($scope.measurements, function(value, key) {
					//Parseo la fecha
					var timestamp = value.timestamp;
					var date = new Date(timestamp);
					var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
					$scope.bdatetime = datevalues;
					///Convierto el punto que quiero pintar para tener su lat y log
					var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
					//Añado al mapa el punto
					L.marker(latlng).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime).openPopup();
				});
			});
		}
		
	} ]);