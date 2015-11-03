-- usuario --
insert into usuario (sourceid) values ('28d2e88199505cc99d1545b1194fbbafa3f754d1f7b27c186a88e22703530ba7');
insert into usuario (sourceid) values ('48e2e88199505cc99d1545b1194fbbafa3f754d1f7b27c186a88e22703530cc0');
insert into usuario (sourceid) values ('88y5e88199505cc99d1545b1194fbbafa3f754d1f7b27c186a88e22703530dd1');
insert into usuario (sourceid) values ('6a4708d3bc8612d7c1c84a8a13df580902f43c0623c765f3acec9d9db88669e9');


-- vehicleLocation --
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(42.33179424 -4.76399282)', 4258),'84b995d5-7136-4084-b405-8075bf1ddabf',3);
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(46.33179424 -5.76399282)', 4258),'5a18ba0b-c484-46cb-a896-e3e6a397f8e3',3);
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(30.33179424 -3.76399282)', 4258),'60ae52db-142f-4b72-be7e-a326d42a9b9e',2);
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(20.33179424 -2.76399282)', 4258),'c43c66f4-8de4-4ce8-9c62-9e069bdaad3b',1);
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(56.33179424 -3.96399282)', 4258),'ecad7a0b-f9f4-47d3-8adc-d7807f0389cb',1);
insert into vehicleLocation (timestamp, position, eventid, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(-8.29857339 -2.99399282)', 4258),'09b89166-0940-49af-8561-7abcd65a5088',1);


-- dataSection --
insert into dataSection (timestamp, roadSection, eventid, idusuario, MinHeartRate, MaxBeatBeat, MaxHeartRate, StandardDeviationSpeed, MinBeatBeat, MinSpeed, AverageSpeed,
 StandardDeviationBeatBeat, HeartRate, MedianSpeed, StandardDeviationHeartRate, MaxSpeed, PKE, MedianHeartRate, MeanBeatBeat, MedianBeatBeat) 
 values ('2015-06-07 00:00:00',ST_GeomFromText('LINESTRING(-70.729212 42.373848,-70.67569 42.375098, -71.67569 43.375098, -72.67569 43.375098)',4258),
'09b89166-0940-49af-8561-d7807f0389cb',1, 75, 84, 84, 17.964899151277262, 75, 53.264455032348636, 92.24283710602792, 2.4879881807181166, 78.68817204301075, 99.72000274658204,
2.7186093576762183, 109.22448120117188, 0.35951261516660654, 78, 79.95046439628483, 80);

insert into dataSection (timestamp, roadSection, eventid, idusuario, MinHeartRate, MaxBeatBeat, MaxHeartRate, StandardDeviationSpeed, MinBeatBeat, MinSpeed, AverageSpeed,
 StandardDeviationBeatBeat, HeartRate, MedianSpeed, StandardDeviationHeartRate, MaxSpeed, PKE, MedianHeartRate, MeanBeatBeat, MedianBeatBeat) 
 values ('2015-06-07 00:00:00',ST_GeomFromText('LINESTRING(-60.729212 32.373848,-60.67569 32.375098, -61.67569 33.375098, -62.67569 33.375098)',4258),
'5a18ba0b-c484-46cb-a896-e3e6a397f8e3',2, 74, 83, 83, 16.964899151277262, 74, 52.264455032348636, 91.24283710602792, 2.3879881807181166, 77.68817204301075, 98.72000274658204,
2.6186093576762183, 100.22448120117188, 0.34951261516660654, 77, 78.95046439628483, 79);

insert into dataSection (timestamp, roadSection, eventid, idusuario, MinHeartRate, MaxBeatBeat, MaxHeartRate, StandardDeviationSpeed, MinBeatBeat, MinSpeed, AverageSpeed,
 StandardDeviationBeatBeat, HeartRate, MedianSpeed, StandardDeviationHeartRate, MaxSpeed, PKE, MedianHeartRate, MeanBeatBeat, MedianBeatBeat) 
 values ('2015-06-07 00:00:00',ST_GeomFromText('LINESTRING(-50.729212 22.373848,-50.67569 22.375098, -51.67569 23.375098, -52.67569 23.375098)',4258),
'09b89166-0940-49af-8561-7abcd65a5088',1, 85, 86, 85, 18.964899151277262, 76, 54.264455032348636, 93.24283710602792, 2.5879881807181166, 79.68817204301075, 100.72000274658204,
3.7186093576762183, 119.22448120117188, 0.45951261516660654, 79, 80.95046439628483, 81);

-- measurement --
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(42.33179424 -4.76399282)', 4258),'84b995d5-7136-4084-b405-8075bf1ddabf',
'HIGH_SPEED',102,3);
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(46.33179424 -5.76399282)', 4258),'5a18ba0b-c484-46cb-a896-e3e6a397f8e3',
'HIGH_ACCELERATION',100,3);
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(30.33179424 -3.76399282)', 4258),'60ae52db-142f-4b72-be7e-a326d42a9b9e',
'HIGH_DECELERATION',110,2);
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(20.33179424 -2.76399282)', 4258),'c43c66f4-8de4-4ce8-9c62-9e069bdaad3b',
'HIGH_ACCELERATION',105,1);
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(56.33179424 -3.96399282)', 4258),'ecad7a0b-f9f4-47d3-8adc-d7807f0389cb',
'HIGH_HEART_RATE',102,1);
insert into measurement (timestamp, position, eventid, tipo, value, idusuario) values ('2015-06-07 00:00:00',ST_PointFromText('POINT(-8.29857339 -2.99399282)', 4258),'09b89166-0940-49af-8561-7abcd65a5088',
'HIGH_ACCELERATION',130,1);

-- eventoProcesado --
insert into eventoProcesado (timestamp, eventid) values ('2015-09-10 11:13:00','b201c340-6afb-4035-a5a4-1a037eaa7b89');

