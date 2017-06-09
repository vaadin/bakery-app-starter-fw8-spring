package com.vaadin.starter.bakery.ui.view.orderedit;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.util.DateTimeFormatter;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@PrototypeScope
public class OrderHistory extends OrderHistoryDesign {

	private final DateTimeFormatter dateTimeFormatter;

	private final ViewEventBus eventBus;

	private Order order;

	private OrderService orderService;

	@Autowired
	public OrderHistory(DateTimeFormatter dateTimeFormatter, ViewEventBus eventBus) {
		this.dateTimeFormatter = dateTimeFormatter;
		this.eventBus = eventBus;
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}

	@PostConstruct
	public void init() {
		// Uses binder to get bean validation for the message
		BeanValidationBinder<HistoryItem> binder = new BeanValidationBinder<>(HistoryItem.class);
		binder.setRequiredConfigurator(null); // Don't show a *
		binder.bind(newCommentInput, "message");
		commitNewComment.addClickListener(e -> {
			if (binder.isValid()) {
				addNewComment(newCommentInput.getValue());
			} else {
				newCommentInput.focus();
			}
		});

		// We don't want a global shortcut for enter, scope it to the panel
		addAction(new ClickShortcut(commitNewComment, KeyCode.ENTER, null));
	}

	public void addNewComment(String comment) {
		getOrderService().addHistoryItem(getOrder(), comment);
		eventBus.publish(this, new OrderUpdated());
	}

	public void setOrder(Order order) {
		this.order = order;
		newCommentInput.setValue("");
		items.removeAllComponents();
		order.getHistory().forEach(historyItem -> {
			Label l = new Label(formatMessage(historyItem));
			l.addStyleName(ValoTheme.LABEL_SMALL);
			l.setCaption(formatTimestamp(historyItem) + " by " + historyItem.getCreatedBy().getName());
			l.setWidth("100%");
			items.addComponent(l);
		});
	}

	private String formatTimestamp(HistoryItem historyItem) {
		return dateTimeFormatter.format(historyItem.getTimestamp(), Locale.US);
	}

	private String formatMessage(HistoryItem historyItem) {
		return historyItem.getMessage();
	}

	public Order getOrder() {
		return order;
	}

}
