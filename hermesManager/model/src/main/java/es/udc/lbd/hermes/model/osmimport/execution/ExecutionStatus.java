package es.udc.lbd.hermes.model.osmimport.execution;

public enum ExecutionStatus {

	ERROR, RUNNING, SUCCESS;

	public String getName(){		
		return this.name();
	}
}
