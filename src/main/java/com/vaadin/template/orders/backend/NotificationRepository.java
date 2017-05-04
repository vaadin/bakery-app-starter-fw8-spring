package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
