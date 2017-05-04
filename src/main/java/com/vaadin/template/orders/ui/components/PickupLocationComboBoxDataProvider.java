package com.vaadin.template.orders.ui.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
public class PickupLocationComboBoxDataProvider extends PageableDataProvider<PickupLocation, String> {

	@Autowired
	private PickupLocationRepository repository;

	private String getNameFilter(Query<PickupLocation, String> query) {
		if (query.getFilter().isPresent()) {
			return "%" + query.getFilter().get() + "%";
		} else {
			return "%";
		}
	}

	@Override
	protected Page<PickupLocation> fetchFromBackEnd(Query<PickupLocation, String> query, Pageable pageable) {
		return repository.findByNameLikeIgnoreCaseOrderByName(getNameFilter(query), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<PickupLocation, String> query) {
		return repository.countByNameLikeIgnoreCase(getNameFilter(query));
	}

}
