package com.vaadin.template.orders.ui.view.orders;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.backend.service.PickupLocationService;
import com.vaadin.template.orders.ui.HasLogger;
import com.vaadin.template.orders.ui.NavigationManager;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.eventbus.ViewEventBus;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@PrototypeScope
public class OrderEditPresenter implements Serializable, HasLogger {

	private OrderEditView view;

	private transient OrderService orderService;

	private transient PickupLocationService pickupLocationService;

	@Autowired
	private NavigationManager navigationManager;

	private static final List<OrderState> happyPath = Arrays.asList(OrderState.NEW, OrderState.CONFIRMED,
			OrderState.READY, OrderState.DELIVERED);
	private final EventBus.ViewEventBus shouldBeGlobalEventBus;

	@Autowired
	public OrderEditPresenter(ViewEventBus viewEventBus, EventBus.ViewEventBus shouldBeGlobalEventBus) {
		viewEventBus.subscribe(ProductInfoChange.class, change -> updateTotalSum());
		viewEventBus.subscribe(OrderItemDeleted.class, deleted -> removeOrderItem(deleted.getOrderItem()));
		this.shouldBeGlobalEventBus = shouldBeGlobalEventBus;
		shouldBeGlobalEventBus.subscribe(this);
	}

	@PreDestroy
	public void destroy() {
		shouldBeGlobalEventBus.unsubscribe(this);
	}

	void init(OrderEditView view) {
		this.view = view;
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
				view.showNotFound();
				return;
			}
		}

		refreshView(order);
	}

	@EventBusListenerMethod
	private void orderUpdated(OrderUpdated event) {
		refresh(view.getOrder().getId());
	}

	private void updateTotalSum() {
		int sum = view.getOrder().getItems().stream().filter(item -> item.getProduct() != null)
				.collect(Collectors.summingInt(item -> item.getProduct().getPrice() * item.getQuantity()));
		view.setSum(sum);
	}

	public void editBackCancelPressed() {
		if (view.getMode() == Mode.REPORT) {
			// Edit order
			view.setMode(Mode.EDIT);
		} else if (view.getMode() == Mode.CONFIRMATION) {
			// Back to edit
			view.setMode(Mode.EDIT);
		} else if (view.getMode() == Mode.EDIT) {
			// Cancel edit
			Long id = view.getOrder().getId();
			if (id == null) {
				navigationManager.navigateTo(OrdersListView.class);
			} else {
				enterView(id);
			}
		}
	}

	public void okPressed() {
		if (view.getMode() == Mode.REPORT) {
			// Set next state
			Order order = view.getOrder();
			Optional<OrderState> nextState = getNextHappyPathState(order.getState());
			if (!nextState.isPresent()) {
				throw new IllegalStateException(
						"The next state button should never be enabled when the state does not follow the happy path");
			}
			getOrderService().changeState(order, nextState.get());
			refresh(order.getId());
		} else if (view.getMode() == Mode.CONFIRMATION) {
			Order order = saveOrder();
			if (order != null) {
				// Navigate to edit view so URL is updated correctly
				navigationManager.navigateTo(OrderEditView.class, order.getId());
			}
		} else if (view.getMode() == Mode.EDIT) {
			Optional<HasValue<?>> firstErrorField = view.validate().findFirst();
			if (firstErrorField.isPresent()) {
				((Focusable) firstErrorField.get()).focus();
				return;
			}
			// New order should still show a confirmation page
			Order order = view.getOrder();
			if (order.getId() == null) {
				filterEmptyProducts();
				view.setMode(Mode.CONFIRMATION);
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
			view.showNotFound();
			return;
		}
		refreshView(order);

	}

	private void refreshView(Order order) {
		view.setOrder(order);
		updateTotalSum();
		if (order.getId() == null) {
			view.setMode(Mode.EDIT);
		} else {
			view.setMode(Mode.REPORT);
		}
	}

	private void filterEmptyProducts() {
		LinkedList<OrderItem> emptyRows = new LinkedList<>();
		view.getOrder().getItems().forEach(orderItem -> {
			if (orderItem.getProduct() == null) {
				emptyRows.add(orderItem);
			}
		});
		emptyRows.forEach(orderItem -> removeOrderItem(orderItem));
	}

	private Order saveOrder() {
		try {
			filterEmptyProducts();
			Order order = view.getOrder();
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
		view.removeOrderItem(orderItem);
		updateTotalSum();
	}
}
