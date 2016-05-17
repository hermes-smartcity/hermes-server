package es.udc.lbd.hermes.model.efficiencytest.dao;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.efficiencytest.EfficiencyTest;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class EfficiencyTestDaoImpl extends GenericDaoHibernate<EfficiencyTest, Long> implements EfficiencyTestDao{

}
