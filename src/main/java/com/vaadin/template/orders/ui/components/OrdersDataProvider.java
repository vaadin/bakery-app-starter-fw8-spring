package com.vaadin.template.orders.ui.components;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersDataProvider extends PageableDataProvider<Order, Object> {

	@Autowired
	private OrderService service;

	@Override
	protected Page<Order> fetchFromBackEnd(Query<Order, Object> query, Pageable pageable) {
		return service.findAfterDueDateWithState(getFilterDate(), getStates(), pageable);
	}

	private List<OrderState> getStates() {
		return Arrays.asList(OrderState.CONFIRMED, OrderState.NEW, OrderState.PROBLEM, OrderState.READY);
	}

	private LocalDate getFilterDate() {
		return LocalDate.now().minusDays(1);
	}

	@Override
	protected int sizeInBackEnd(Query<Order, Object> query) {
		return (int) service.countAfterDueDateWithState(getFilterDate(), getStates());
	}

}
