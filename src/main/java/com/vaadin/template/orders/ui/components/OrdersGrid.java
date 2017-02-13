package com.vaadin.template.orders.ui.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    private final transient DateTimeFormatter dueDateFormat = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT);

    public OrdersGrid() {
        setSizeFull();
        addStyleName("two-row");
        setColumnResizeMode(ColumnResizeMode.ANIMATED);
        Column<Order, String> dueColumn = addColumn(
                order -> twoRowCell(getTimeHeader(order.getDue()),
                        order.getDue().format(dueDateFormat)),
                new HtmlRenderer());
        dueColumn.setCaption("Due").setWidthUndefined();

        addColumn(order -> {
            Customer customer = order.getCustomer();
            return twoRowCell(
                    customer.getFirstName() + " " + customer.getLastName(),
                    getOrderSummary(order));
        }, new HtmlRenderer()).setExpandRatio(1);

        Column<Order, String> stateColumn = addColumn(
                order -> order.getState().toString());
        stateColumn.setStyleGenerator(
                order -> "status " + order.getState().name().toLowerCase());
    }

    private String getTimeHeader(LocalDateTime due) {
        LocalDate dueDate = due.toLocalDate();
        LocalDate today = LocalDate.now();
        if (dueDate.isEqual(today)) {
            return "Today";
        } else {
            // Show weekday for upcoming days
            LocalDate todayNextWeek = today.plusDays(7);
            if (dueDate.isAfter(today) && dueDate.isBefore(todayNextWeek)) {
                return dueDate.getDayOfWeek().getDisplayName(TextStyle.SHORT,
                        Locale.ENGLISH);
            } else {
                // In the past or more than a week in the future
                return "";
            }
        }
    }

    private String getOrderSummary(Order order) {
        Stream<String> quantityAndName = order.getItems().stream()
                .map(item -> item.getQuantity() + "x "
                        + item.getProduct().getName());
        return quantityAndName.collect(Collectors.joining(", "));
    }

    private String twoRowCell(String header, String content) {
        return "<b>" + header + "</b><br>" + content;
    }

}
