package com.vaadin.template.orders.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;

@SpringComponent
public class PickupLocationService {

	@Autowired
	private PickupLocationRepository pickupLocationRepository;

	public Page<PickupLocation> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.findByNameLikeIgnoreCaseOrderByName(repositoryFilter, pageable);
		} else {
			return pickupLocationRepository.findByNameLikeIgnoreCaseOrderByName("%", pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return pickupLocationRepository.countByNameLikeIgnoreCase("%");
		}
	}

}
