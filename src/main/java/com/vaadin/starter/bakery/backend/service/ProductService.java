package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.ProductRepository;
import com.vaadin.starter.bakery.backend.data.entity.Product;

@Service
public class ProductService extends CrudService<Product> {

	@Override
	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return getRepository().count();
		}
	}

	@Override
	protected ProductRepository getRepository() {
		return BeanLocator.find(ProductRepository.class);
	}

	@Override
	public Page<Product> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}
}
