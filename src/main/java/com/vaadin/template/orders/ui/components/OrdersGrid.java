package com.vaadin.template.orders.ui.components;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;

public class OrdersGrid extends Grid<Order> {

	public OrdersGrid() {
		setSizeFull();
		addStyleName("two-row");
		setColumnResizeMode(ColumnResizeMode.ANIMATED);
		removeHeaderRow(0);

		// Due
		Column<Order, String> dueColumn = addColumn(order -> dueCell(getTimeHeader(order.getDueDate()),
				String.valueOf(order.getDueTime()), getTimeStyle(order.getDueDate())), new HtmlRenderer());
		dueColumn.setSortProperty("dueDate", "dueTime");

		// Order
		addColumn(order -> {
			Customer customer = order.getCustomer();
			return twoRowCell(customer.getFullName(), getOrderSummary(order));
		}, new HtmlRenderer()).setExpandRatio(1).setSortProperty("customer.fullName");

		// Status
		Column<Order, String> stateColumn = addColumn(order -> "<div>" + order.getState() + "</div>")
				.setRenderer(new HtmlRenderer());
		stateColumn.setStyleGenerator(order -> "status " + order.getState().name().toLowerCase());
		stateColumn.setSortProperty("state");
	}

	private String getTimeHeader(LocalDate dueDate) {
		LocalDate today = LocalDate.now();
		if (dueDate.isEqual(today)) {
			return "Today";
		} else {
			// Show weekday for upcoming days
			LocalDate todayNextWeek = today.plusDays(7);
			if (dueDate.isAfter(today) && dueDate.isBefore(todayNextWeek)) {
				// "Mon 7"
				return dueDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + " "
						+ dueDate.getDayOfMonth();
			} else {
				// In the past or more than a week in the future
				return dueDate.getDayOfMonth() + " " + dueDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
			}
		}
	}

	private String getTimeStyle(LocalDate dueDate) {
		int i = LocalDate.now().until(dueDate).getDays();
		if (i < 7) {
			return "in" + i;
		} else {
			return "";
		}
	}

	private String getOrderSummary(Order order) {
		Stream<String> quantityAndName = order.getItems().stream()
				.map(item -> item.getQuantity() + "x " + item.getProduct().getName());
		return quantityAndName.collect(Collectors.joining(", "));
	}

	private String dueCell(String header, String content, String styleName) {
		return "<div class=\"due " + styleName + "\"><b>" + header + "</b>" + content + "</div>";
	}

	private String twoRowCell(String header, String content) {
		return "<b>" + header + "</b><br>" + content;
	}

}
