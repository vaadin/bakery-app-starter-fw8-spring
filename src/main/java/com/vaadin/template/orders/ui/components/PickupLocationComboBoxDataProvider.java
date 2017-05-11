package com.vaadin.template.orders.ui.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.backend.service.PickupLocationService;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
public class PickupLocationComboBoxDataProvider extends PageableDataProvider<PickupLocation, String> {

	@Autowired
	private PickupLocationService service;

	@Override
	protected Page<PickupLocation> fetchFromBackEnd(Query<PickupLocation, String> query, Pageable pageable) {
		return service.findAnyMatching(query.getFilter(), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<PickupLocation, String> query) {
		return (int) service.countAnyMatching(query.getFilter());
	}

}
