package com.vaadin.template.orders.backend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.PickupLocation;

public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {

	Page<PickupLocation> findByNameLikeIgnoreCaseOrderByName(String nameFilter, Pageable pageable);

	int countByNameLikeIgnoreCase(String nameFilter);
}
