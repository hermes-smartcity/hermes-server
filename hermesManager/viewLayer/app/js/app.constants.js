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
var url_vehicleLocationsGrouped = url_vehicleL+'json/vehicleLocationsGrouped?';
var url_eventosPorDiaVL = url_vehicleL+'json/eventosPorDia?';
		
//Controlador dataSection
var url_dataS = url_servidor+'api/datasection/';
var url_dataSections = url_dataS+'json/dataSections?';
var url_eventosPorDiaDS = url_dataS+'json/eventosPorDia?';

//Controlador measurement
var url_measurement = url_servidor+'api/measurement/';
var url_measurements = url_measurement+'json/measurements';
var url_eventosPorDiaM = url_measurement+'json/eventosPorDia?';
var url_measurementsGrouped = url_measurement+'json/measurementsGrouped';

//Controlador contextdata
var url_contextD = url_servidor+'api/contextdata/';
var url_contextData = url_contextD+'json/contextData?';
var url_eventosPorDiaCD = url_contextD+'json/eventosPorDia?';
var url_contextDataGrouped = url_contextD+'json/contextDataGrouped?';

//Controlador userLocations
var url_userL = url_servidor+'api/userlocation/';
var url_userLocations = url_userL+'json/userLocations?';
var url_eventosPorDiaUL = url_userL+'json/eventosPorDia?';
var url_userLocationsGrouped = url_userL+'json/userLocationsGrouped?';

//Controlador userActivities
var url_userA = url_servidor+'api/useractivity/';
var url_userActivities = url_userA+'json/userActivities?';
var url_eventosPorDiaUA = url_userA+'json/eventosPorDia?';

//Controlador driverFeatures
var url_driverF = url_servidor+'api/driverfeature/';
var url_driverFeatures = url_driverF+'json/driverFeatures?';
var url_eventosPorDiaDF = url_driverF+'json/eventosPorDia?';

//Controlador sleepData
var url_sleepD = url_servidor+'api/sleepdata/';
var url_sleepData = url_sleepD+'json/sleepData?';
var url_eventosPorDiaSLD = url_sleepD+'json/eventosPorDia?';

//Controlador stepsData
var url_stepsD = url_servidor+'api/stepsdata/';
var url_stepsData = url_stepsD+'json/stepsData?';
var url_eventosPorDiaSTD = url_stepsD+'json/eventosPorDia?';

//Controlador heartRateData
var url_heartRD = url_servidor+'api/heartratedata/';
var url_heartRateData = url_heartRD+'json/heartRateData?';
var url_eventosPorDiaHRD = url_heartRD+'json/eventosPorDia?';

//Controlador systemLogs
var url_systemL = url_servidor+'api/systemlog/';
var url_systemLogs = url_systemL+'json/systemLogs?';
var url_delete_log = url_systemL+"deleteLog";

//Controlador settings
var url_setting = url_servidor+'api/setting/';
var url_settings = url_setting+'json/settings';
var url_update_settings = url_setting + "updateSettings";

//Controlador hermes services
var url_hermesS = url_servidor+'api/hermes/';
var url_hermesS_services = url_hermesS+'json/services';
var url_hermesS_methods = url_hermesS+'json/methods';
var url_hermesS_types = url_hermesS+'json/types';
var url_hermesS_measurementTypes = url_hermesS+'json/measurementTypes';
var url_datasections = url_hermesS+'json/datasections';
var url_network_link = url_hermesS+'network/link?';
var url_measurement_aggregate = url_hermesS+'measurement/aggregate?';
var url_network_route = url_hermesS+'network/route?';
var url_get_vehicle_locations = url_hermesS + 'vehiclelocation?';
var url_get_measurements = url_hermesS + 'measurement?';
var url_get_data_sections = url_hermesS + 'datasection?';
var url_get_driver_features = url_hermesS + 'driverfeatures?';
var url_get_heart_rate_data = url_hermesS + 'heartratedata?';
var url_get_steps_data = url_hermesS + 'stepsdata?';
var url_get_sleep_data = url_hermesS + 'sleepdata?';
var url_get_context_data = url_hermesS + 'contextdata?';
var url_get_user_locations = url_hermesS + 'userlocations?';
var url_get_user_activities = url_hermesS + 'useractivities?';

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

//Controlador db connections
var url_dbconnection = url_servidor+'api/dbconnection/';
var url_dbConnectionsTypes = url_dbconnection+'json/dbConnectionsTypes';
var url_dbConnections = url_dbconnection+'json/dbConnections';
var url_delete_dbconnection = url_dbconnection+"delete";
var url_register_dbconnection = url_dbconnection+"register";
var url_edit_dbconnection = url_dbconnection+"edit";
var url_get_dbconnection = url_dbconnection+'json/dbConnection';

//Controlador db concepts
var url_dbconcept = url_servidor+'api/dbconcept/';
var url_dbConcepts = url_dbconcept+'json/dbConcepts';
var url_delete_dbconcept = url_dbconcept+"delete";
var url_register_dbconcept = url_dbconcept+"register";
var url_edit_dbconcept = url_dbconcept+"edit";
var url_get_dbconcept = url_dbconcept+'json/dbConcept';

//Controlador db attributes
var url_dbattribute = url_servidor+'api/dbattribute/';
var url_dbAttributesTypes = url_dbattribute+'json/dbAttributesTypes';
var url_dbAttributes = url_dbattribute+'json/dbAttributes';
var url_delete_dbattribute = url_dbattribute+"delete";
var url_register_dbattribute = url_dbattribute+"register";
var url_edit_dbattribute = url_dbattribute+"edit";
var url_get_dbattribute = url_dbattribute+'json/dbAttribute';

//Controlador osm concepts
var url_osmconcept = url_servidor+'api/osmconcept/';
var url_osmConcepts = url_osmconcept+'json/osmConcepts';
var url_delete_osmconcept = url_osmconcept+"delete";
var url_register_osmconcept = url_osmconcept+"register";
var url_edit_osmconcept = url_osmconcept+"edit";
var url_get_osmconcept = url_osmconcept+'json/osmConcept';

//Controlador osm attributes
var url_osmattribute = url_servidor+'api/osmattribute/';
var url_osmAttributes = url_osmattribute+'json/osmAttributes';
var url_delete_osmattribute = url_osmattribute+"delete";
var url_register_osmattribute = url_osmattribute+"register";
var url_edit_osmattribute = url_osmattribute+"edit";
var url_get_osmattribute = url_osmattribute+'json/osmAttribute';

//Controlador osm filter
var url_osmfilter = url_servidor+'api/osmfilter/';
var url_osmFiltersOperation = url_osmfilter+'json/osmFiltersOperation';
var url_osmFilters = url_osmfilter+'json/osmFilters';
var url_delete_osmfilter = url_osmfilter+"delete";
var url_register_osmfilter = url_osmfilter+"register";
var url_edit_osmfilter = url_osmfilter+"edit";
var url_get_osmfilter = url_osmfilter+'json/osmFilter';

//Controlador job
var url_job = url_servidor+'api/job/';
var url_jobs = url_job+'json/jobs';
var url_delete_job = url_job+"delete";
var url_register_job = url_job+"register";
var url_edit_job = url_job+"edit";
var url_get_job = url_job+'json/job';
var url_execute_job = url_job+"executeJob";
var url_launch_execute_job = url_job+"launchExecuteJob";


//Controlador concept transformation
var url_concepttransformation = url_servidor+'api/concepttransformation/';
var url_concepttransformations = url_concepttransformation+'json/concepttransformations';
var url_delete_concepttransformation = url_concepttransformation+"delete";
var url_register_concepttransformation = url_concepttransformation+"register";
var url_edit_concepttransformation = url_concepttransformation+"edit";
var url_get_concepttransformation = url_concepttransformation+'json/concepttransformation';

//Controlador attributemapping
var url_attributemapping = url_servidor+'api/attributemapping/';
var url_attributemappings = url_attributemapping+'json/attributemappings';
var url_delete_attributemapping = url_attributemapping+"delete";
var url_register_attributemapping = url_attributemapping+"register";
var url_edit_attributemapping = url_attributemapping+"edit";
var url_get_attributemapping = url_attributemapping+'json/attributemapping';

//Controlador executions
var url_execution = url_servidor+'api/execution/';
var url_executions = url_execution+'json/executions';
var url_delete_execution = url_execution+"delete";

//Controlador messages
var url_message = url_servidor+'api/message/';
var url_messages = url_message+'json/messages';
var url_messages_with_status = url_message+'json/messagesstatus';
