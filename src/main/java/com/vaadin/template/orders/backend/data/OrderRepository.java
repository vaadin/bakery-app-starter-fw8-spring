package com.vaadin.template.orders.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.app.OrderInfo;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
}
