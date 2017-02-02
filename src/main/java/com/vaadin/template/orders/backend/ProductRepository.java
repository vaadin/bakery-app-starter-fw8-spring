package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
