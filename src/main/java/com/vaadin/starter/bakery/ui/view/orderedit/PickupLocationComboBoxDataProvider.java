package com.vaadin.starter.bakery.ui.view.orderedit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;
import com.vaadin.starter.bakery.ui.dataprovider.PageableDataProvider;

/**
 * A singleton data provider which knows which products are available.
 */
@SpringComponent
public class PickupLocationComboBoxDataProvider extends PageableDataProvider<PickupLocation, String> {

	private transient PickupLocationService pickupLocationService;

	@Override
	protected Page<PickupLocation> fetchFromBackEnd(Query<PickupLocation, String> query, Pageable pageable) {
		return getPickupLocationService().findAnyMatching(query.getFilter(), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<PickupLocation, String> query) {
		return (int) getPickupLocationService().countAnyMatching(query.getFilter());
	}

	protected PickupLocationService getPickupLocationService() {
		if (pickupLocationService == null) {
			pickupLocationService = BeanLocator.find(PickupLocationService.class);
		}
		return pickupLocationService;
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("name", SortDirection.ASCENDING));
		return sortOrders;
	}

}