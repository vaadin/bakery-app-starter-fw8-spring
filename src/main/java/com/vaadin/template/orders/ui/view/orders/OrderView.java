package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.ui.components.DollarPriceFormatter;

@SpringView
public class OrderView extends OrderViewDesign implements View {

    public String paid;

    @Autowired
    private OrderPresenter presenter;
    @Autowired
    private ObjectProvider<ProductInfo> productInfoProvider;

    private Binder<Order> binder;

    @Autowired
    private DollarPriceFormatter formatter;

    private final List<ProductInfo> productInfos = new ArrayList<>();

    @PostConstruct
    public void init() {
        presenter.init(this);
        pickupLocation.setItems(presenter.getPickupLocations());
        pickupLocation.setEmptySelectionAllowed(false);
        pickupLocation.setItemCaptionGenerator(location -> location.getName());

        // Binds properties in Order automatically:
        // pickupLocation, state, paid
        // Does not bind sub properties, see
        // https://github.com/vaadin/framework/issues/8384
        binder = new Binder<>(Order.class);
        binder.bindInstanceFields(this);
        binder.forField(firstName).bind(
                order -> order.getCustomer().getFirstName(),
                (order, value) -> order.getCustomer().setFirstName(value));
        binder.forField(lastName).bind(
                order -> order.getCustomer().getLastName(),
                (order, value) -> order.getCustomer().setLastName(value));
        binder.forField(phone).bind(
                order -> order.getCustomer().getPhoneNumber(),
                (order, value) -> order.getCustomer().setPhoneNumber(value));
        binder.forField(email).bind(order -> order.getCustomer().getEmail(),
                (order, value) -> order.getCustomer().setEmail(value));
        binder.forField(phone).bind(
                order -> order.getCustomer().getPhoneNumber(),
                (order, value) -> order.getCustomer().setPhoneNumber(value));
        binder.forField(details).bind(order -> order.getCustomer().getDetails(),
                (order, value) -> order.getCustomer().setDetails(value));

        binder.forField(dueDateTime).bind(order -> {
            return LocalDateTime.of(order.getDueDate(), order.getDueTime());
        }, (order, value) -> {
            order.setDueDate(value.toLocalDate());
            order.setDueTime(value.toLocalTime());
        });

        addItems.addClickListener(e -> {
            productInfoContainer
                    .addComponent(createProductInfo(new OrderItem()));
        });

    }

    @Override
    public void enter(ViewChangeEvent event) {
        String orderId = event.getParameters();
        if ("".equals(orderId)) {
            presenter.enterView(null);
        } else {
            presenter.enterView(Long.valueOf(orderId));
        }
    }

    public void editOrder(Order order) {
        productInfoContainer.removeAllComponents();

        if (order.getId() == null) {
            newCustomer.setVisible(true);
            productInfoContainer
                    .addComponent(createProductInfo(new OrderItem()));
        } else {
            newCustomer.setVisible(false);
            for (OrderItem item : order.getItems()) {
                ProductInfo productInfo = createProductInfo(item);
                productInfoContainer.addComponent(productInfo);
            }
        }
        updateTotalSum();
        binder.setBean(order);

    }

    /**
     * Create a ProductInfo instance using Spring so that it is injected and can
     * in turn inject a ProductComboBox and its data provider.
     *
     * @param orderItem
     *            the item to edit
     *
     * @return a new product info instance
     */
    private ProductInfo createProductInfo(OrderItem orderItem) {
        ProductInfo productInfo = productInfoProvider.getObject();
        productInfo.setItem(orderItem);
        productInfos.add(productInfo);
        return productInfo;
    }

    public void updateTotalSum() {
        double sum = 0;
        for (ProductInfo productInfo : productInfos) {
            sum += productInfo.getSum();
        }
        total.setValue(formatter.format(sum, Locale.US));
    }

}
