package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.ui.components.DollarPriceFormatter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@SpringView(name = "order")
public class OrderEditView extends OrderEditViewDesign implements View {

    @Autowired
    private OrderEditPresenter presenter;

    @Autowired
    private ObjectProvider<ProductInfo> productInfoProvider;

    @Autowired
    private DollarPriceFormatter formatter;

    private BeanValidationBinder<Order> binder;

    private Mode mode;

    @PostConstruct
    public void init() {
        presenter.init(this);
        pickupLocation.setItems(presenter.getPickupLocations());
        pickupLocation.setEmptySelectionAllowed(false);
        pickupLocation.setItemCaptionGenerator(PickupLocation::getName);

        // Binds properties in Order automatically:
        // pickupLocation, state, paid
        // Does not bind sub properties, see
        // https://github.com/vaadin/framework/issues/8384
        binder = new BeanValidationBinder<>(Order.class);

        // Bindings are done in the order the fields appear on the screen as we
        // report validation errors for the first invalid field and it is most
        // intuitive for the user that we start from the top if there are
        // multiple errors.
        binder.forField(dueDateTime).asRequired("You must select a date")
                .bind(order -> {
                    if (order.getDueDate() == null
                            || order.getDueTime() == null) {
                        return null;
                    }

                    return LocalDateTime.of(order.getDueDate(),
                            order.getDueTime());
                }, (order, value) -> {
                    order.setDueDate(value.toLocalDate());
                    order.setDueTime(value.toLocalTime());
                });
        binder.bindInstanceFields(this);
        SubPropertyBinder.bind(binder, firstName, "customer.firstName",
                Customer.class);
        SubPropertyBinder.bind(binder, lastName, "customer.lastName",
                Customer.class);
        SubPropertyBinder.bind(binder, phone, "customer.phoneNumber",
                Customer.class);
        SubPropertyBinder.bind(binder, email, "customer.email", Customer.class);
        SubPropertyBinder.bind(binder, details, "customer.details",
                Customer.class);

        addItems.addClickListener(e -> addEmptyOrderItem());
        editBackCancel.addClickListener(e -> presenter.editBackCancelPressed());
        ok.addClickListener(e -> presenter.okPressed());
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
        binder.setBean(order);
        productInfoContainer.removeAllComponents();

        if (order.getId() == null) {
            newCustomer.setVisible(true);
            addEmptyOrderItem();
            dueDateTime.focus();
        } else {
            newCustomer.setVisible(false);
            for (OrderItem item : order.getItems()) {
                ProductInfo productInfo = createProductInfo(item);
                productInfoContainer.addComponent(productInfo);
            }
        }
    }

    private void addEmptyOrderItem() {
        OrderItem orderItem = new OrderItem();
        productInfoContainer.addComponent(createProductInfo(orderItem));
        getOrder().getItems().add(orderItem);
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
        return productInfo;
    }

    protected Order getOrder() {
        return binder.getBean();
    }

    protected void setSum(double sum) {
        total.setValue(formatter.format(sum, Locale.US));
    }

    public void showNotFound() {
        removeAllComponents();
        addComponent(new Label("Order not found"));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        binder.setReadOnly(mode != Mode.EDIT);
        for (Component c : productInfoContainer) {
            if (c instanceof ProductInfo) {
                ((ProductInfo) c).setReportMode(mode != Mode.EDIT);
            }
        }
        addItems.setVisible(mode == Mode.EDIT);

        reportHeader.setVisible(mode == Mode.REPORT);

        if (mode == Mode.REPORT) {
            editBackCancel.setCaption("Edit");
            ok.setCaption("<next status>");
        } else if (mode == Mode.CONFIRMATION) {
            editBackCancel.setCaption("< Back");
            ok.setCaption("Place order");
        } else {
            editBackCancel.setCaption("Cancel");
            ok.setCaption("Done");
        }
    }

    public Mode getMode() {
        return mode;
    }

    public Stream<HasValue<?>> validate() {
        Stream<HasValue<?>> errorFields = binder.validate()
                .getFieldValidationErrors().stream()
                .map(BindingValidationStatus::getField);

        for (Component c : productInfoContainer) {
            if (c instanceof ProductInfo) {
                errorFields = Stream.concat(errorFields,
                        ((ProductInfo) c).validate());
            }
        }
        return errorFields;
    }
}
