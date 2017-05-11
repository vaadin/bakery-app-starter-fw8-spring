package com.vaadin.template.orders.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.template.orders.backend.CustomerRepository;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserService userService;

	public Order findOrder(Long id) {
		return orderRepository.findOne(id);
	}

	public Order changeState(Order order, OrderState state) {
		if (order.getState() == state) {
			throw new IllegalArgumentException("Order state is already " + state);
		}
		order.setState(state);
		addHistoryItem(order, state);

		return orderRepository.save(order);
	}

	private void addHistoryItem(Order order, OrderState newState) {
		String comment = "Order " + newState.getDisplayName();

		HistoryItem item = new HistoryItem(userService.getCurrentUser(), comment);
		item.setNewState(newState);
		if (order.getHistory() == null) {
			order.setHistory(new ArrayList<>());
		}
		order.getHistory().add(item);
	}

	@Transactional(rollbackOn = Exception.class)
	public Order saveOrder(Order order) {
		// FIXME Use existing customer maybe
		Customer customer = customerRepository.save(order.getCustomer());
		order.setCustomer(customer);

		if (order.getHistory() == null) {
			String comment = "Order placed";
			order.setHistory(new ArrayList<>());
			HistoryItem item = new HistoryItem(userService.getCurrentUser(), comment);
			item.setNewState(OrderState.NEW);
			order.getHistory().add(item);
		}
		return orderRepository.save(order);

	}

	public Order addHistoryItem(Order order, String comment) {
		HistoryItem item = new HistoryItem(userService.getCurrentUser(), comment);
		if (order.getHistory() == null) {
			order.setHistory(new ArrayList<>());
		}
		order.getHistory().add(item);

		return orderRepository.save(order);

	}

	public Page<Order> findAfterDueDateWithState(LocalDate filterDate, List<OrderState> states, Pageable pageable) {
		return orderRepository.findByDueDateAfterAndStateInOrderByDueDateAscDueTimeDescIdDesc(filterDate, states,
				pageable);
	}

	public long countAfterDueDateWithState(LocalDate filterDate, List<OrderState> states) {
		return orderRepository.countByDueDateAfterAndStateIn(filterDate, states);
	}

}
