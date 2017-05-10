package com.vaadin.template.orders.ui.view.orders;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.DollarPriceConverter;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.eventbus.ViewEventBus;

@SpringComponent
@PrototypeScope
public class ProductInfo extends ProductInfoDesign {

	@Autowired
	private DollarPriceConverter priceFormatter;

	@Autowired
	private ViewEventBus viewEventBus;

	private BeanValidationBinder<OrderItem> binder;

	@PostConstruct
	public void init() {
		binder = new BeanValidationBinder<>(OrderItem.class);
		binder.forField(quantity).withConverter(new StringToIntegerConverter("Please enter a number")).bind("quantity");
		binder.bindInstanceFields(this);
		binder.addValueChangeListener(e -> {
			if (e.getComponent() == quantity || e.getComponent() == product) {
				fireProductInfoChanged();
			}
		});

		product.addSelectionListener(e -> {
			Optional<Product> selectedProduct = e.getFirstSelectedItem();
			int productPrice = selectedProduct.map(Product::getPrice).orElse(0);
			updatePrice(productPrice);
		});

	}

	private void updatePrice(int productPrice) {
		price.setValue(priceFormatter.convertToPresentation(productPrice, new ValueContext(Locale.US)));
	}

	private void fireProductInfoChanged() {
		viewEventBus.publish(new ProductInfoChange());
	}

	public double getSum() {
		OrderItem item = binder.getBean();
		return item.getQuantity() * item.getProduct().getPrice();
	}

	public void setItem(OrderItem item) {
		binder.setBean(item);
	}

	public void setReportMode(boolean reportMode) {
		binder.setReadOnly(reportMode);
		comment.setVisible(!(reportMode && comment.isEmpty()));
	}

	public Stream<HasValue<?>> validate() {
		return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
	}

	@Override
	public void focus() {
		product.focus();
	}
}
