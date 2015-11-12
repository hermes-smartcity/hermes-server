var dataSectionApp = angular.module('dataSectionApp', ['ngRoute']);
var dataSectionApp = angular.module('dataSectionApp', []);


dataSectionApp.controller('DataSectionsController', [ '$scope', '$http',
                                     
	function($scope, $http) {
	
		$scope.getDataSections = function() {
			var idUsuario = getUrlParameter("idUsuario");
			var urlGet = "json/dataSections";
			if(idUsuario != null)
				urlGet = "json/dataSectionsByUsuario?"+"idUsuario="+idUsuario;

			$http.get(urlGet).success(function(data) {
				$scope.dataSections = data;
				
				var map = L.map('map').setView([51.505, -0.09], 5);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);

				//Recorro el array geojson de cada uno de los dataSections
				angular.forEach($scope.dataSections, function(value, key) {
					//Parseo la fecha
					var timestamp = value.timestamp;
					var date = new Date(timestamp);
					var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
					$scope.bdatetime = datevalues;
					//Almaceno array de puntos 
					var myLines = value.roadSection;
					var myStyle = {
						    "color": "#ff7800",
						    "weight": 4,
						    "opacity": 0.65
						};
					var myLayer = L.geoJson().addTo(map);
					L.geoJson(myLines, {
					    style: myStyle
					}).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime).openPopup();;
					/**/
				});
			});
		}
		
	} ]);