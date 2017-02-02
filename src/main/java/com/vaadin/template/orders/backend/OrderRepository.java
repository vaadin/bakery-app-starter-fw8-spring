package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
