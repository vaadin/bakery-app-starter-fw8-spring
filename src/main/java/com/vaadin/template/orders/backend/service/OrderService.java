package com.vaadin.template.orders.backend.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.view.dashboard.DashboardData;
import com.vaadin.template.orders.ui.view.dashboard.DeliveryStats;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserService userService;
	private static Set<OrderState> notAvailableStates;
	static {
		notAvailableStates = new HashSet<>(Arrays.asList(OrderState.values()));
		notAvailableStates.remove(OrderState.DELIVERED);
		notAvailableStates.remove(OrderState.READY_FOR_PICKUP);
		notAvailableStates.remove(OrderState.CANCELLED);
	}

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

	private DeliveryStats getDeliveryStats() {
		DeliveryStats stats = new DeliveryStats();
		LocalDate today = LocalDate.now();
		stats.setDueToday((int) orderRepository.countByDueDate(today));
		stats.setDueTomorrow((int) orderRepository.countByDueDate(today.plusDays(1)));
		stats.setDeliveredToday(
				(int) orderRepository.countByDueDateAndStateIn(today, Collections.singleton(OrderState.DELIVERED)));

		stats.setNotAvailableToday((int) orderRepository.countByDueDateAndStateIn(today, notAvailableStates));
		stats.setNewOrders((int) orderRepository.countByState(OrderState.NEW));

		return stats;
	}

	public DashboardData getDashboardData(int month, int year) {
		DashboardData data = new DashboardData();
		data.setDeliveryStats(getDeliveryStats());
		data.setDeliveriesThisMonth(getDeliveriesPerDay(month, year));
		data.setDeliveriesThisYear(getDeliveriesPerMonth(year));

		Number[][] salesPerMonth = new Number[3][12];
		data.setSalesPerMonth(salesPerMonth);
		List<Object[]> sales = orderRepository.sumPerMonthLastThreeYears(OrderState.DELIVERED, year);

		for (Object[] salesData : sales) {
			// year, month, deliveries
			int y = year - (int) salesData[0];
			int m = (int) salesData[1] - 1;
			long count = (long) salesData[2];
			salesPerMonth[y][m] = count;
		}

		LinkedHashMap<Product, Integer> productDeliveries = new LinkedHashMap<>();
		data.setProductDeliveries(productDeliveries);
		for (Object[] result : orderRepository.countPerProduct(OrderState.DELIVERED, year, month)) {
			int sum = ((Long) result[0]).intValue();
			Product p = (Product) result[1];
			productDeliveries.put(p, sum);
		}

		return data;
	}

	private List<Number> getDeliveriesPerDay(int month, int year) {
		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		return flattenAndReplaceMissingWithNull(daysInMonth,
				orderRepository.countPerDay(OrderState.DELIVERED, year, month));
	}

	private List<Number> getDeliveriesPerMonth(int year) {
		return flattenAndReplaceMissingWithNull(12, orderRepository.countPerMonth(OrderState.DELIVERED, year));
	}

	private List<Number> flattenAndReplaceMissingWithNull(int length, List<Object[]> list) {
		List<Number> counts = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			counts.add(null);
		}

		for (Object[] result : list) {
			counts.set((Integer) result[0] - 1, (Number) result[1]);
		}
		return counts;
	}
}
