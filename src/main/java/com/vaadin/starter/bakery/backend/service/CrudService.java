package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

public abstract class CrudService<T extends AbstractEntity> {

	protected abstract CrudRepository<T, Long> getRepository();

	public T save(T entity) {
		return getRepository().save(entity);
	}

	public void delete(long id) {
		getRepository().deleteById(id);
	}

	public T load(long id) {
		return getRepository().findById(id).orElseThrow(EntityNotFoundException::new);
	}

	public abstract long countAnyMatching(Optional<String> filter);

	public abstract Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

}
