package es.udc.lbd.hermes.model.sensordata.dao;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class SensorDataDaoImpl extends GenericDaoHibernate<SensorData, Long> implements SensorDataDao{

}
