package com.vaadin.template.orders.ui.view.orders;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.DollarPriceFormatter;
import com.vaadin.template.orders.ui.components.ProductComboBox;

@SpringComponent
@PrototypeScope
public class ProductInfo extends ProductInfoDesign {

    @Autowired
    private DollarPriceFormatter priceFormatter;

    @Autowired
    private ProductComboBox product;

    @Autowired
    private StringToIntegerConverter converter;

    // @Autowired
    // private EventBus.ViewEventBus eventBus;

    private Binder<OrderItem> binder;

    @PostConstruct
    public void init() {
        addComponent(product, 0, 0);

        binder = new Binder<>(OrderItem.class);
        product.addSelectionListener(e -> {
            Optional<Product> selectedProduct = e.getFirstSelectedItem();
            double productPrice = selectedProduct.map(Product::getPrice)
                    .orElse(0.0);
            updatePrice(productPrice);
        });
        quantity.addValueChangeListener(e -> fireProductInfoChanged());

        binder.forField(quantity).withConverter(converter).bind("quantity");
        binder.bindInstanceFields(this);
    }

    private void updatePrice(double productPrice) {
        price.setValue(priceFormatter.format(productPrice, Locale.US));
        fireProductInfoChanged();
    }

    private void fireProductInfoChanged() {
        // eventBus.publish(this, new ProductInfoChangeEvent());
    }

    public double getSum() {
        OrderItem item = binder.getBean();
        return item.getQuantity() * item.getProduct().getPrice();
    }

    public void setItem(OrderItem item) {
        binder.setBean(item);
    }

}
