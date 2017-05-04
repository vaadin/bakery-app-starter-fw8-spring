package com.vaadin.template.orders.ui.components;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.framework.FrameworkDataHelper;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class OrdersDataProvider extends AbstractBackEndDataProvider<Order, Object> {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	protected Stream<Order> fetchFromBackEnd(Query<Order, Object> query) {
		int firstRequested = query.getOffset();
		int nrRequested = query.getLimit();
		Pageable pageable = FrameworkDataHelper.getPageable(query);

		List<Order> items = orderRepository
				.findByDueDateAfterAndStateInOrderByDueDateAscDueTimeDescIdDesc(getFilterDate(), getStates(), pageable)
				.getContent();
		int firstReturned = pageable.getPageNumber();
		int firstReal = firstRequested - firstReturned;
		int afterLastReal = firstReal + nrRequested;
		if (afterLastReal > items.size()) {
			afterLastReal = items.size();
		}
		return items.subList(firstReal, afterLastReal).stream();

	}

	private List<OrderState> getStates() {
		return Arrays.asList(OrderState.CONFIRMED, OrderState.NEW, OrderState.PROBLEM, OrderState.READY_FOR_PICKUP);
	}

	private LocalDate getFilterDate() {
		return LocalDate.now().minusDays(1);
	}

	@Override
	protected int sizeInBackEnd(Query<Order, Object> query) {
		return (int) orderRepository.countByDueDateAfterAndStateIn(getFilterDate(), getStates());
	}

}
