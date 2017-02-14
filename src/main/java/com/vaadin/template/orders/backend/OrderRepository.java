package com.vaadin.template.orders.backend;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByDueAfterAndStateInOrderByDueAsc(LocalDateTime date,
            Collection<OrderState> states, Pageable page);

    long countByDueAfterAndStateIn(LocalDateTime date,
            Collection<OrderState> states);
}
