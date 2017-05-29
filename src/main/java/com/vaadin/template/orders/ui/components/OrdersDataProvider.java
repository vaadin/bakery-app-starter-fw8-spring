package com.vaadin.template.orders.ui.components;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.dataprovider.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersDataProvider extends PageableDataProvider<Order, Object> {

	private transient OrderService orderService;

	@Override
	protected Page<Order> fetchFromBackEnd(Query<Order, Object> query, Pageable pageable) {
		return getOrderService().findAfterDueDate(getFilterDate(), pageable);
	}

	private LocalDate getFilterDate() {
		return LocalDate.now().minusDays(1);
	}

	@Override
	protected int sizeInBackEnd(Query<Order, Object> query) {
		return (int) getOrderService().countAfterDueDate(getFilterDate());
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("dueDate", SortDirection.ASCENDING));
		sortOrders.add(new QuerySortOrder("dueTime", SortDirection.DESCENDING));
		// id included only to always get a stable sort order
		sortOrders.add(new QuerySortOrder("id", SortDirection.DESCENDING));
		return sortOrders;
	}
}
