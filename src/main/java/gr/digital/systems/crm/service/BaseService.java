package gr.digital.systems.crm.service;

import gr.digital.systems.crm.model.BaseEntity;
import java.util.List;

public interface BaseService<T extends BaseEntity> {
	T create(final T entity);

	List<T> createAll(final T... entities);

	List<T> createAll(final List<T> entities);

	T update(final T entity);

	T delete(final T entity);

	T deleteById(final Long id);

	boolean exists(final T entity);

	T get(final Long id);

	List<T> findAll();
}
