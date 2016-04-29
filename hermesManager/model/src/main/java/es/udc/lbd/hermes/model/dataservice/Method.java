package es.udc.lbd.hermes.model.dataservice;

import java.util.EnumSet;

public enum Method {

	GET_INFORMATION_LINK, AGGREGATE_MEASUREMENT, COMPUTE_ROUTE;
	
	public static EnumSet<Method> smartDriver = EnumSet.of(GET_INFORMATION_LINK, AGGREGATE_MEASUREMENT);
	public static EnumSet<Method> smartCitizen = EnumSet.of(COMPUTE_ROUTE);
	
	public String getName(){		
		return this.name();
	}
}
