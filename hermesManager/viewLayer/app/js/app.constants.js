'use strict';

/* no_concrete_routing para eliminar as rutas e que non se poida recargar unha vista concreta */
var no_concrete_routing = true;

var debug_mode = true;

var url_servidor = 'http://localhost:8080/eventManager/';

var url_eventManager = url_servidor+'api/eventManager/';
var url_arrancar = url_eventManager+'arrancar';
var url_parar = url_eventManager+'parar';
var url_state = url_eventManager+'json/stateEventManager';
var url_user = url_eventManager+'json/stateEventManager';

var url_dashboard = url_servidor+'api/dashboard/';
var url_eventsToday = url_dashboard+'json/eventsToday';
var url_eventsType = url_dashboard+'json/eventsType';
var url_usuarios = url_dashboard+'json/usuarios';
var url_measurementTypes = url_dashboard+'json/measurementTypes';
var url_eventoProcesado = url_dashboard+'json/eventoProcesado';
var url_totalVLocations = url_dashboard+'json/totalVLocations';
var url_totalDataScts = url_dashboard+'json/totalDataScts';
var url_totalMeasurements = url_dashboard+'json/totalMeasurements';
var url_totalDriversF = url_dashboard+'json/totalDriversF';

var url_vehicleL = url_servidor+'api/vehiclelocation/';
var url_vehicleLocations = url_vehicleL+'vehiclelocation/json/vehicleLocations?';

var url_dataS = url_servidor+'api/datasection/';
var url_dataSections = url_dataS+'datasection/json/dataSections?';

var url_measurement = url_servidor+'api/measurement/';
var url_measurements = url_measurement+'measurement/json/measurements';