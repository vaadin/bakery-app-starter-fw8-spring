package com.vaadin.starter.bakery.ui.view.orderedit;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Customer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.eventbus.ViewEventBus;
import com.vaadin.starter.bakery.ui.navigation.NavigationEvent;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.util.DollarPriceConverter;
import com.vaadin.starter.bakery.ui.view.NavigableView;
import com.vaadin.starter.bakery.ui.view.storefront.StorefrontView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@ViewScope
@SpringView(name = "order-edit")
public class OrderEditController implements NavigableView, Serializable, HasLogger {

	public enum Mode {
		EDIT, REPORT, CONFIRMATION;
	}

	private final OrderEditViewDesign view;

	private transient OrderService orderService;

	private transient PickupLocationService pickupLocationService;

	private final NavigationManager navigationManager;

	private static final List<OrderState> happyPath = Arrays.asList(OrderState.NEW, OrderState.CONFIRMED,
			OrderState.READY, OrderState.DELIVERED);

	private final DollarPriceConverter priceConverter;

	private BeanValidationBinder<Order> binder;

	private Mode mode;

	private boolean hasChanges;

	@Autowired
	public OrderEditController(ViewEventBus viewEventBus, NavigationManager navigationManager,
			DollarPriceConverter priceConverter) {
		view = new OrderEditViewDesign();
		this.navigationManager = navigationManager;
		this.priceConverter = priceConverter;
		viewEventBus.subscribe(ProductInfoChangeEvent.class, change -> {
			updateTotalSum();
			onProductInfoChanged();
		});
		viewEventBus.subscribe(OrderItemDeleted.class, deleted -> {
			removeOrderItem(deleted.getOrderItem());
			onProductInfoChanged();
		});
		viewEventBus.subscribe(OrderUpdated.class, deleted -> {
			refresh(getOrder().getId());
		});
	}

	@PostConstruct
	public void init() {
		// We're limiting dueTime to even hours between 07:00 and 17:00
		view.dueTime.setItems(IntStream.range(7, 17).mapToObj(h -> LocalTime.of(h, 0)));

		// Binds properties in Order automatically:
		// pickupLocation, state, paid
		// Does not bind sub properties, see
		// https://github.com/vaadin/framework/issues/8384
		binder = new BeanValidationBinder<>(Order.class);

		// Almost all fields are required, so we don't want to display
		// indicators
		binder.setRequiredConfigurator(null);

		// Bindings are done in the order the fields appear on the screen as we
		// report validation errors for the first invalid field and it is most
		// intuitive for the user that we start from the top if there are
		// multiple errors.
		binder.bindInstanceFields(view);

		// These can be removed once
		// https://github.com/vaadin/framework/issues/9210 is fixed
		binder.bind(view.fullName, "customer.fullName");
		binder.bind(view.phone, "customer.phoneNumber");
		binder.bind(view.details, "customer.details");

		// Track changes manually as we use setBean and nested binders
		binder.addValueChangeListener(e -> hasChanges = true);

		view.addItems.addClickListener(e -> addEmptyOrderItem());
		view.cancel.addClickListener(e -> editBackCancelPressed());
		view.ok.addClickListener(e -> okPressed());
	}

	/**
	 * Called when the user enters the view.
	 */
	public void enterView(Long id) {
		Order order;
		if (id == null) {
			// New
			order = new Order();
			order.setState(OrderState.NEW);
			order.setItems(new ArrayList<>());
			order.setCustomer(new Customer());
			order.setDueDate(LocalDate.now().plusDays(1));
			order.setDueTime(LocalTime.of(8, 00));
			order.setPickupLocation(getPickupLocationService().getDefault());
		} else {
			order = getOrderService().findOrder(id);
			if (order == null) {
				showNotFound();
				return;
			}
		}

		refreshView(order);
	}

	private void updateTotalSum() {
		int sum = getOrder().getItems().stream().filter(item -> item.getProduct() != null)
				.collect(Collectors.summingInt(item -> item.getProduct().getPrice() * item.getQuantity()));
		setSum(sum);
	}

	public void editBackCancelPressed() {
		if (getMode() == Mode.REPORT) {
			// Edit order
			setMode(Mode.EDIT);
		} else if (getMode() == Mode.CONFIRMATION) {
			// Back to edit
			setMode(Mode.EDIT);
		} else if (getMode() == Mode.EDIT) {
			// Cancel edit
			Long id = getOrder().getId();
			if (id == null) {
				navigationManager.navigateTo(StorefrontView.class);
			} else {
				enterView(id);
			}
		}
	}

	public void okPressed() {
		if (getMode() == Mode.REPORT) {
			// Set next state
			Order order = getOrder();
			Optional<OrderState> nextState = getNextHappyPathState(order.getState());
			if (!nextState.isPresent()) {
				throw new IllegalStateException(
						"The next state button should never be enabled when the state does not follow the happy path");
			}
			getOrderService().changeState(order, nextState.get());
			refresh(order.getId());
		} else if (getMode() == Mode.CONFIRMATION) {
			Order order = saveOrder();
			if (order != null) {
				// Navigate to edit view so URL is updated correctly
				navigationManager.updateViewParameter("" + order.getId());
				enterView(order.getId());
			}
		} else if (getMode() == Mode.EDIT) {
			Optional<HasValue<?>> firstErrorField = validate().findFirst();
			if (firstErrorField.isPresent()) {
				((Focusable) firstErrorField.get()).focus();
				return;
			}
			// New order should still show a confirmation page
			Order order = getOrder();
			if (order.getId() == null) {
				filterEmptyProducts();
				setMode(Mode.CONFIRMATION);
			} else {
				order = saveOrder();
				if (order != null) {
					refresh(order.getId());
				}
			}
		}
	}

	private void refresh(Long id) {
		Order order = getOrderService().findOrder(id);
		if (order == null) {
			showNotFound();
			return;
		}
		refreshView(order);

	}

	private void refreshView(Order order) {
		setOrder(order);
		updateTotalSum();
		if (order.getId() == null) {
			setMode(Mode.EDIT);
		} else {
			setMode(Mode.REPORT);
		}
	}

	private void filterEmptyProducts() {
		LinkedList<OrderItem> emptyRows = new LinkedList<>();
		getOrder().getItems().forEach(orderItem -> {
			if (orderItem.getProduct() == null) {
				emptyRows.add(orderItem);
			}
		});
		emptyRows.forEach(this::removeOrderItem);
	}

	private Order saveOrder() {
		try {
			filterEmptyProducts();
			Order order = getOrder();
			return getOrderService().saveOrder(order);

		} catch (ValidationException e) {
			// Should not get here if validation is setup properly
			Notification.show("Please check the contents of the fields: " + e.getMessage(), Type.ERROR_MESSAGE);
			getLogger().error("Validation error during order save", e);
			return null;
		} catch (Exception e) {
			Notification.show("Somebody else might have updated the data. Please refresh and try again.",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to save order", e);
			return null;
		}
	}

	public Optional<OrderState> getNextHappyPathState(OrderState current) {
		final int currentIndex = happyPath.indexOf(current);
		if (currentIndex == -1 || currentIndex == happyPath.size() - 1) {
			return Optional.empty();
		}
		return Optional.of(happyPath.get(currentIndex + 1));
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}

	protected PickupLocationService getPickupLocationService() {
		if (pickupLocationService == null) {
			pickupLocationService = BeanLocator.find(PickupLocationService.class);
		}
		return pickupLocationService;
	}

	private void removeOrderItem(OrderItem orderItem) {
		getOrder().getItems().remove(orderItem);

		for (Component c : view.productInfoContainer) {
			if (c instanceof ProductInfo && ((ProductInfo) c).getItem() == orderItem) {
				view.productInfoContainer.removeComponent(c);
				break;
			}
		}
		updateTotalSum();
	}

	@Override
	public Component getViewComponent() {
		return view;
	}

	public void setOrder(Order order) {
		view.stateLabel.setValue(order.getState().getDisplayName());
		binder.setBean(order);
		view.productInfoContainer.removeAllComponents();

		view.reportHeader.setVisible(order.getId() != null);
		if (order.getId() == null) {
			addEmptyOrderItem();
			view.dueDate.focus();
		} else {
			view.orderId.setValue("#" + order.getId());
			for (OrderItem item : order.getItems()) {
				ProductInfo productInfo = createProductInfo(item);
				productInfo.setReportMode(mode != Mode.EDIT);
				view.productInfoContainer.addComponent(productInfo);
			}
			view.history.setOrder(order);
		}
		hasChanges = false;
	}

	private void addEmptyOrderItem() {
		OrderItem orderItem = new OrderItem();
		ProductInfo productInfo = createProductInfo(orderItem);
		view.productInfoContainer.addComponent(productInfo);
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
		ProductInfo productInfo = BeanLocator.find(ProductInfo.class);
		productInfo.setItem(orderItem);
		return productInfo;
	}

	protected Order getOrder() {
		return binder.getBean();
	}

	protected void setSum(int sum) {
		view.total.setValue(priceConverter.convertToPresentation(sum, new ValueContext(Locale.US)));
	}

	public void showNotFound() {
		view.removeAllComponents();
		view.addComponent(new Label("Order not found"));
	}

	public void setMode(Mode mode) {
		// Allow to style different modes separately
		if (this.mode != null) {
			view.removeStyleName(this.mode.name().toLowerCase());
		}
		view.addStyleName(mode.name().toLowerCase());

		this.mode = mode;
		binder.setReadOnly(mode != Mode.EDIT);
		for (Component c : view.productInfoContainer) {
			if (c instanceof ProductInfo) {
				((ProductInfo) c).setReportMode(mode != Mode.EDIT);
			}
		}
		view.addItems.setVisible(mode == Mode.EDIT);
		view.history.setVisible(mode == Mode.REPORT);
		view.state.setVisible(mode == Mode.EDIT);

		if (mode == Mode.REPORT) {
			view.cancel.setCaption("Edit");
			view.cancel.setIcon(VaadinIcons.EDIT);
			Optional<OrderState> nextState = getNextHappyPathState(getOrder().getState());
			view.ok.setCaption("Mark as " + nextState.map(OrderState::getDisplayName).orElse("?"));
			view.ok.setVisible(nextState.isPresent());
		} else if (mode == Mode.CONFIRMATION) {
			view.cancel.setCaption("Back");
			view.cancel.setIcon(VaadinIcons.ANGLE_LEFT);
			view.ok.setCaption("Place order");
			view.ok.setVisible(true);
		} else if (mode == Mode.EDIT) {
			view.cancel.setCaption("Cancel");
			view.cancel.setIcon(VaadinIcons.CLOSE);
			if (getOrder() != null && !getOrder().isNew()) {
				view.ok.setCaption("Save");
			} else {
				view.ok.setCaption("Review order");
			}
			view.ok.setVisible(true);
		} else {
			throw new IllegalArgumentException("Unknown mode " + mode);
		}
	}

	public Mode getMode() {
		return mode;
	}

	public Stream<HasValue<?>> validate() {
		Stream<HasValue<?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

		for (Component c : view.productInfoContainer) {
			if (c instanceof ProductInfo) {
				ProductInfo productInfo = (ProductInfo) c;
				if (!productInfo.isEmpty()) {
					errorFields = Stream.concat(errorFields, productInfo.validate());
				}
			}
		}
		return errorFields;
	}

	public void onProductInfoChanged() {
		hasChanges = true;
	}

	public boolean containsUnsavedChanges() {
		return hasChanges;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String orderId = event.getParameters();
		if ("".equals(orderId)) {
			enterView(null);
		} else {
			enterView(Long.valueOf(orderId));
		}
	}

	@Override
	public boolean beforeLeave(NavigationEvent event) {
		if (!containsUnsavedChanges()) {
			return true;
		}

		ConfirmationDialog.show(getViewComponent().getUI(), event::navigate);
		return false;
	}
}
