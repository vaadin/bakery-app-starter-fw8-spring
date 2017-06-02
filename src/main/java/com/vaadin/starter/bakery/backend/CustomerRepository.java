package com.vaadin.starter.bakery.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
