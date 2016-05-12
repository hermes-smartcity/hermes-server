package es.udc.lbd.hermes.model.testExitsTableQuery.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestExitsTableQueryDaoImpl implements TestExitsTableQueryDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public Boolean exitsSchemaTable(String schema, String table){
		
		String queryStr = "SELECT EXISTS ( " +
							  "SELECT 1 " +
							  "FROM information_schema.tables " +
							  "WHERE table_schema = :schema " +
							  "AND table_name = :table " +
						  ")";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		
		query.setParameter("schema", schema);
		query.setParameter("table", table);
		
		return (Boolean) query.uniqueResult();
	}
	
	public Boolean existsColumnTable(String schema, String table, String column){
		String queryStr = "SELECT EXISTS ( " +
				  "SELECT 1 " +
				  "FROM information_schema.columns " +
				  "WHERE table_schema = :schema " +
				  "AND table_name = :table " +
				  "AND column_name = :column " +
			  ")";

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		
		query.setParameter("schema", schema);
		query.setParameter("table", table);
		query.setParameter("column", column);
		
		return (Boolean) query.uniqueResult();
	}
	
	public Boolean existsTypeColumn(String schema, String table, String column, List<String> types){
		String queryStr = "SELECT EXISTS ( " +
				  "SELECT 1 " +
				  "FROM information_schema.columns " +
				  "WHERE table_schema = :schema " +
				  "AND table_name = :table " +
				  "AND column_name = :column " +
				  "AND data_type IN (:types) " +
			  ")";

		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		
		query.setParameter("schema", schema);
		query.setParameter("table", table);
		query.setParameter("column", column);
		query.setParameterList("types", types);
		
		return (Boolean) query.uniqueResult();
	}
}
