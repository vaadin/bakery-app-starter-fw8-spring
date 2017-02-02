package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.HistoryItem;

public interface HistoryItemRepository
        extends JpaRepository<HistoryItem, Long> {
}
