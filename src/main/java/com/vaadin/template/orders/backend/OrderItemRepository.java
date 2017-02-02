package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
