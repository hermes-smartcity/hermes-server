package es.udc.lbd.hermes.model.testExitsTableQuery.dao;

import java.util.List;


public interface TestExitsTableQueryDao{

	public Boolean exitsSchemaTable(String schema, String table);
	public Boolean existsColumnTable(String schema, String table, String column);
	public Boolean existsTypeColumn(String schema, String table, String column, List<String> types);
}
