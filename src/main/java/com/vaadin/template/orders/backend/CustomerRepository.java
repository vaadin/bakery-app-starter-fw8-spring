package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
