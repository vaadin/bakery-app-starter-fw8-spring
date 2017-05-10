package com.vaadin.template.orders.ui.view.orders;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.app.DollarPriceConverter;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.ui.view.OrdersView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@SpringView(name = "order")
public class OrderEditView extends OrderEditViewDesign implements OrdersView {

	@Autowired
	private OrderEditPresenter presenter;

	@Autowired
	private ObjectProvider<ProductInfo> productInfoProvider;

	@Autowired
	private DollarPriceConverter priceConverter;

	private BeanValidationBinder<Order> binder;

	private Mode mode;

	@Autowired
	private ObjectProvider<OrderStateWindow> windowProvider;

	@PostConstruct
	public void init() {
		presenter.init(this);

		// Binds properties in Order automatically:
		// pickupLocation, state, paid
		// Does not bind sub properties, see
		// https://github.com/vaadin/framework/issues/8384
		binder = new BeanValidationBinder<>(Order.class);

		// Bindings are done in the order the fields appear on the screen as we
		// report validation errors for the first invalid field and it is most
		// intuitive for the user that we start from the top if there are
		// multiple errors.
		binder.bindInstanceFields(this);

		// These can be removed once
		// https://github.com/vaadin/framework/issues/9210 is fixed
		binder.bind(firstName, "customer.firstName");
		binder.bind(lastName, "customer.lastName");
		binder.bind(phone, "customer.phoneNumber");
		binder.bind(email, "customer.email");
		binder.bind(details, "customer.details");

		addItems.addClickListener(e -> addEmptyOrderItem());
		cancel.addClickListener(e -> presenter.editBackCancelPressed());
		ok.addClickListener(e -> presenter.okPressed());

		setState.addClickListener(e -> {
			OrderStateWindow w = windowProvider.getObject();
			w.setOrder(getOrder());
			getUI().addWindow(w);
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

	public void setOrder(Order order) {
		orderState.setValue(order.getState().getDisplayName());
		setState.setVisible(order.getId() != null);
		binder.setBean(order);
		productInfoContainer.removeAllComponents();

		reportHeader.setVisible(order.getId() != null);
		if (order.getId() == null) {
			addEmptyOrderItem();
			dueDate.focus();
		} else {
			orderId.setValue("#" + order.getId());
			for (OrderItem item : order.getItems()) {
				ProductInfo productInfo = createProductInfo(item);
				productInfo.setReportMode(mode != Mode.EDIT);
				productInfoContainer.addComponent(productInfo);
			}
			history.setOrder(order);
		}
	}

	private void addEmptyOrderItem() {
		OrderItem orderItem = new OrderItem();
		ProductInfo productInfo = createProductInfo(orderItem);
		productInfoContainer.addComponent(productInfo);
		productInfo.focus();
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

	protected void setSum(int sum) {
		total.setValue(priceConverter.convertToPresentation(sum, new ValueContext(Locale.US)));
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
		history.setVisible(mode == Mode.REPORT);
		setState.setVisible(mode == Mode.REPORT);

		if (mode == Mode.REPORT) {
			cancel.setCaption("Edit");
			Optional<OrderState> nextState = presenter.getNextHappyPathState(getOrder().getState());
			ok.setCaption(nextState.map(OrderState::getDisplayName).orElse("?"));
			ok.setVisible(nextState.isPresent());
		} else if (mode == Mode.CONFIRMATION) {
			cancel.setCaption("< Back");
			ok.setCaption("Place order");
			ok.setVisible(true);
		} else {
			cancel.setCaption("Cancel");
			ok.setCaption("Done");
			ok.setVisible(true);
		}
	}

	public Mode getMode() {
		return mode;
	}

	public Stream<HasValue<?>> validate() {
		Stream<HasValue<?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

		for (Component c : productInfoContainer) {
			if (c instanceof ProductInfo) {
				errorFields = Stream.concat(errorFields, ((ProductInfo) c).validate());
			}
		}
		return errorFields;
	}
}
