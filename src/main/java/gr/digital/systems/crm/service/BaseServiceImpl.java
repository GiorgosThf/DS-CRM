package gr.digital.systems.crm.service;

import gr.digital.systems.crm.component.BaseComponent;
import gr.digital.systems.crm.exception.CrmException;
import gr.digital.systems.crm.model.BaseEntity;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.READ_COMMITTED,
		rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T extends BaseEntity> extends BaseComponent
		implements BaseService<T> {

	public abstract JpaRepository<T, Long> getRepository();

	@SafeVarargs
	@Override
	public final List<T> createAll(final T... items) {
		return this.createAll(Arrays.asList(items));
	}

	@Override
	public List<T> createAll(final List<T> items) {
		return this.getRepository().saveAll(items);
	}

	@Override
	public T create(final T item) {
		logger.trace("Creating {}.", item);
		return this.getRepository().save(item);
	}

	@Override
	public T update(final T item) {
		logger.trace("Updating {}.", item);
		return this.getRepository().save(item);
	}

	@Override
	public T delete(final T item) {
		final T itemFound = this.getRepository().getReferenceById(item.getId());
		logger.trace("Deleting {}.", itemFound);
		this.getRepository().delete(itemFound);
		return itemFound;
	}

	@Override
	public T deleteById(final Long id) {
		final T itemFound = getRepository().getReferenceById(id);
		logger.trace("Deleting {}.", itemFound);
		this.getRepository().deleteById(id);
		return itemFound;
	}

	@Transactional(readOnly = true)
	@Override
	public boolean exists(final T item) {
		logger.trace("Checking whether {} exists.", item);
		return this.getRepository().existsById(item.getId());
	}

	@Transactional(readOnly = true)
	@Override
	public T get(final Long id) {
		logger.trace("Retrieving item with id {}.", id);
		return this.getRepository()
				.findById(id)
				.orElseThrow(() -> new CrmException("Element not found"));
	}

	@Transactional(readOnly = true)
	@Override
	public List<T> findAll() {
		logger.trace("Retrieving all items.");
		return this.getRepository().findAll();
	}
}
