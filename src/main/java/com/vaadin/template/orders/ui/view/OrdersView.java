package com.vaadin.template.orders.ui.view;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.HtmlRenderer;

@SpringView
public class OrdersView extends OrdersViewDesign implements View {

    @Autowired
    private OrdersPresenter presenter;

    private transient DateTimeFormatter dueDataFormat = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT);

    @PostConstruct
    public void init() {
        list.addStyleName("two-row");
        list.setDataProvider(presenter.getOrdersProvider());

        Column<Order, String> dueColumn = list.addColumn(
                order -> twoRowCell("Today",
                        order.getDue().format(dueDataFormat)),
                new HtmlRenderer());
        dueColumn.setCaption("Due").setWidthUndefined();

        list.addColumn(order -> {
            Customer customer = order.getCustomer();
            return twoRowCell(
                    customer.getFirstName() + " " + customer.getLastName(),
                    getOrderSummary(order));
        }, new HtmlRenderer()).setExpandRatio(1);

        list.addColumn(order -> order.getState().toString())
                .setStyleGenerator(order -> {
                    return "status " + order.getState().name().toLowerCase();
                });

        presenter.init(this);
    }

    private String getOrderSummary(Order order) {
        Stream<String> quantityAndName = order.getItems().stream().map(item -> {
            return item.getQuantity() + "x " + item.getProduct().getName();
        });
        return quantityAndName.collect(Collectors.joining(","));
    }

    private String twoRowCell(String header, String content) {
        return "<b>" + header + "</b><br>" + content;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
