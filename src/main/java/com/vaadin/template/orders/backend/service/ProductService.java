package com.vaadin.template.orders.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.data.entity.Product;

@SpringComponent
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Page<Product> find(Pageable pageable) {
		return productRepository.findBy(pageable);
	}

	public long count() {
		return productRepository.count();
	}

	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.findByNameLikeIgnoreCaseOrderByName(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	public Product get(Long id) {
		return productRepository.findOne(id);
	}

	public void delete(Long id) {
		productRepository.delete(id);
	}

	public Product save(Product product) {
		return productRepository.save(product);
	}

}
