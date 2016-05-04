package es.udc.lbd.hermes.model.osmimport.attributemapping.dao;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class AttributeMappingDaoImpl extends GenericDaoHibernate<AttributeMapping, Long> implements AttributeMappingDao{

}
