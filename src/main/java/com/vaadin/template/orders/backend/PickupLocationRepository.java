package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.PickupLocation;

public interface PickupLocationRepository
        extends JpaRepository<PickupLocation, Long> {
}
