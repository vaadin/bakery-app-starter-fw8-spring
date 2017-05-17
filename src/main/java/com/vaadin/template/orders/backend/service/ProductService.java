package com.vaadin.template.orders.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.data.entity.Product;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public Page<Product> find(Pageable pageable) {
		return getProductRepository().findBy(pageable);
	}

	public long count() {
		return getProductRepository().count();
	}

	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getProductRepository().findByNameLikeIgnoreCaseOrderByName(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getProductRepository().countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	public Product get(Long id) {
		return getProductRepository().findOne(id);
	}

	public void delete(Long id) {
		getProductRepository().delete(id);
	}

	public Product save(Product product) {
		return getProductRepository().save(product);
	}

	protected ProductRepository getProductRepository() {
		if (productRepository == null) {
			productRepository = BeanLocator.find(ProductRepository.class);
		}
		return productRepository;
	}
}
