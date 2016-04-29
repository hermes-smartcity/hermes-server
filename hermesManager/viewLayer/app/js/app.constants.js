'use strict';

/* no_concrete_routing para eliminar as rutas e que non se poida recargar unha vista concreta */
var no_concrete_routing = true;

var debug_mode = true;

var url_servidor = '%%SERVER_URL%%';


////Controlador authenticate
//var url_authenticate = url_servidor+'api/authenticate';

//Controlador user
//var url_user = url_servidor+'api/user';
//var url_get_user = url_user;
//var url_register_user = url_user;
//var url_delete_user = url_user;
//var url_infoCuenta = url_servidor+'api/json/infoCuenta';
//var url_roles = url_user+'/json/roles';
//var url_users = url_user+'/json/users';
//var url_admins = url_user+'/json/admins';
//var url_userToModify = url_user+'/json/userToModify';
/**/
var url_user = url_servidor+'api/user/';
var url_authenticate = url_user+'authenticate';
var url_get_user = url_user+"getUser";
var url_register_user = url_user+"registerUser";
var url_register_admin = url_user+"registerAdmin";
var url_edit_user = url_user+"editUser";
var url_delete_user = url_user+"deleteUser";
var url_users = url_user+'json/users';
var url_admins = url_user+'json/admins';
var url_roles = url_user+'json/roles';
var url_userToModify = url_user+'json/userToModify';
var url_infoCuenta = url_user+'activarCuenta';
var url_renewToken = url_user+"renewToken";
var url_change_password = url_user+"changePassword";
var url_user_profile = url_user+"userProfile";

// Controlador event manager
var url_eventManager = url_servidor+'api/eventManager/';
var url_arrancar = url_eventManager+'arrancar';
var url_parar = url_eventManager+'parar';
var url_state = url_eventManager+'json/stateEventManager';


//Controlador dashboard
var url_dashboard = url_servidor+'api/dashboard/';
var url_eventsToday = url_dashboard+'json/eventsToday';
var url_eventsType = url_dashboard+'json/eventsType';
var url_measurementTypes = url_dashboard+'json/measurementTypes';
var url_usuarios = url_dashboard+'json/usuarios';
var url_eventoProcesado = url_dashboard+'json/eventoProcesado';
var url_parameters_statistics = url_dashboard+'json/parametersStatistics';
var url_sensorsTypes = url_dashboard+'json/sensorsType';


//Controlador vehicleLocation
var url_vehicleL = url_servidor+'api/vehiclelocation/';
var url_vehicleLocations = url_vehicleL+'json/vehicleLocations?';
var url_eventosPorDiaVL = url_vehicleL+'json/eventosPorDia?';
		
//Controlador dataSection
var url_dataS = url_servidor+'api/datasection/';
var url_dataSections = url_dataS+'json/dataSections?';
var url_eventosPorDiaDS = url_dataS+'json/eventosPorDia?';

//Controlador measurement
var url_measurement = url_servidor+'api/measurement/';
var url_measurements = url_measurement+'json/measurements';
var url_eventosPorDiaM = url_measurement+'json/eventosPorDia?';

//Controlador contextdata
var url_contextD = url_servidor+'api/contextdata/';
var url_contextData = url_contextD+'json/contextData?';
var url_eventosPorDiaCD = url_contextD+'json/eventosPorDia?';

//Controlador userLocations
var url_userL = url_servidor+'api/userlocation/';
var url_userLocations = url_userL+'json/userLocations?';
var url_eventosPorDiaUL = url_userL+'json/eventosPorDia?';

//Controlador userActivities
var url_userA = url_servidor+'api/useractivity/';
var url_userActivities = url_userA+'json/userActivities?';
var url_eventosPorDiaUA = url_userA+'json/eventosPorDia?';

//Controlador systemLogs
var url_systemL = url_servidor+'api/systemlog/';
var url_systemLogs = url_systemL+'json/systemLogs?';
var url_delete_log = url_systemL+"deleteLog";

//Controlador settings
var url_setting = url_servidor+'api/setting/';
var url_settings = url_setting+'json/settings';
var url_update_settings = url_setting + "updateSettings";

//Controlador smartdrive
var url_smartdriver = url_servidor+'api/smartdriver/';
var url_smartdriver_services = url_smartdriver+'json/services';
var url_smartdriver_methods = url_smartdriver+'json/methods';
var url_types = url_smartdriver+'json/types';
var url_datasections = url_smartdriver+'json/datasections';
var url_network_link = url_smartdriver+'network/link?';
var url_measurement_aggregate = url_smartdriver+'measurement/aggregate?';
var url_network_route = url_smartdriver+'network/route?';

//Controlador dataservice
var url_dataservice = url_servidor+'api/dataservice/';
var url_services = url_dataservice+'json/services';
var url_operations = url_dataservice+'json/operations';
var url_peticionesPorDia = url_dataservice+'json/peticionesPorDia?';

//Controlador de sensordata
var url_sensordata = url_servidor + 'api/sensordata/';
var url_request_sensors = url_sensordata + 'sensors';
var url_request_gps = url_sensordata + 'gps';
var url_infoPorDia = url_sensordata + 'json/infoPorDia?';
var url_gpslocation = url_sensordata+'json/gpslocations?';