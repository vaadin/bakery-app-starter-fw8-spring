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
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.DollarPriceConverter;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.eventbus.ViewEventBus;
import com.vaadin.ui.Label;

@SpringComponent
@PrototypeScope
public class ProductInfo extends ProductInfoDesign {

	@Autowired
	private DollarPriceConverter priceFormatter;

	@Autowired
	private ViewEventBus viewEventBus;

	private BeanValidationBinder<OrderItem> binder;

	// We'll display this instead of a TextArea in "report mode" for a better
	// presentation
	private Label readOnlyComment = new Label();

	private boolean reportMode = false;

	@PostConstruct
	public void init() {
		binder = new BeanValidationBinder<>(OrderItem.class);
		binder.setRequiredConfigurator(null);
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

		readOnlyComment.setWidth("100%");
		readOnlyComment.setId(comment.getId());
	}

	private void updatePrice(int productPrice) {
		price.setValue(priceFormatter.convertToPresentation(productPrice, new ValueContext(Locale.US)));
	}

	private void fireProductInfoChanged() {
		viewEventBus.publish(new ProductInfoChange());
	}

	public int getSum() {
		OrderItem item = binder.getBean();
		return item.getQuantity() * item.getProduct().getPrice();
	}

	public void setItem(OrderItem item) {
		binder.setBean(item);
	}

	public void setReportMode(boolean reportMode) {
		if (reportMode == this.reportMode) {
			return;
		}
		this.reportMode = reportMode;
		binder.setReadOnly(reportMode);

		// Swap the TextArea for a Label in report mode
		if (reportMode) {
			readOnlyComment.setVisible(!comment.isEmpty());
			readOnlyComment.setValue(comment.getValue());
			replaceComponent(comment, readOnlyComment);
		} else {
			replaceComponent(readOnlyComment, comment);
		}
	}

	/**
	 * Checks if no product has been selected. If no product is selected, the
	 * whole product info section is ignored when saving changes.
	 *
	 * @return <code>true</code> if no product is selected, <code>false</code>
	 *         otherwise
	 */
	public boolean isEmpty() {
		return product.isEmpty();
	}

	public Stream<HasValue<?>> validate() {
		return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
	}

	@Override
	public void focus() {
		product.focus();
	}
}
