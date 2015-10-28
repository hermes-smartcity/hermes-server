package es.enxenio.smart.model.util.dao;

import java.io.Serializable;

public interface GenericDao <E, PK extends Serializable>{

	void create(E entity);

	E get(PK id);

	boolean exists(PK id);

	void update(E entity);

	void delete(PK id);
	
	void save(E entity);

}