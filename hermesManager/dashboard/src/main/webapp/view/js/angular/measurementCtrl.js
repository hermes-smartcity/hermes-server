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
				
			var map = L.map('map');
			map.fitBounds([
			               [ -43.334162, -8.413220],
			               [-43.334162, -8.413220]
			           ]);
			L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
			
			recuperarEventos(urlGet);
			map.on('dragend', onChangeBounds);
			map.on('zoomend', onChangeBounds);
			
			function onChangeBounds(e) {
				var bounds = map.getBounds();				
				var esLng = bounds.getSouthEast().lng;
				var esLat = bounds.getSouthEast().lat;
				var wnLng = bounds.getNorthWest().lng;
				var wnLat = bounds.getNorthWest().lat;
			
				var urlGet = "json/measurementsByBounds?tipo="+tipoMeasurement+"&wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;
				if(idUsuario != null)
					urlGet = "json/measurementsByUsuarioByBounds&bounds="+poligono+"?idUsuario="+idUsuario;
				
				recuperarEventos(urlGet);
			}
		
		
		function recuperarEventos(urlGet){
			var mystyles = {
				    color: 'red',
				    fillOpacity: 0.1
				};
			$http.get(urlGet).success(function(data) {
				$scope.measurements = data;
				
				
				angular.forEach($scope.measurements, function(value, key) {
					//Parseo la fecha
					var timestamp = value.timestamp;
					var date = new Date(timestamp);
					var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
					$scope.bdatetime = datevalues;
					///Convierto el punto que quiero pintar para tener su lat y log
					var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
					//Añado al mapa el punto
					var circle = L.circle(latlng, 5, mystyles).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime);
//					L.marker(latlng).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime);
				});
			});
		}
		}
	} ]);