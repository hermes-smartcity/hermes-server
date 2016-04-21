package es.udc.lbd.hermes.model.gps.dao;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class GpsDaoImp extends GenericDaoHibernate<Gps, Long> implements GpsDao{

}
