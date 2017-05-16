package com.vaadin.template.orders.ui.view.orders;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.DateTimeFormatter;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.Label;

@SpringComponent
@PrototypeScope
public class OrderHistory extends OrderHistoryDesign {
	
	// IDs for testing purposes
	public static final String COMMENT_INPUT_ID = "commentInput";
	public static final String COMMIT_COMMENT_ID = "commitCommentButton";

	@Autowired
	private DateTimeFormatter dateTimeFormatter;
	@Autowired
	private OrderHistoryController controller;

	private Order order;

	@PostConstruct
	public void init() {
		controller.init(this);

		newCommentInput.setId(COMMENT_INPUT_ID);
		commitNewComment.setId(COMMIT_COMMENT_ID);
		
		commitNewComment.addClickListener(e -> controller.addNewComment(newCommentInput.getValue()));
		
		// We don't want t global shortcut for enter, so we add it to the wrapping panel
		historyPanel.addAction(new ClickShortcut(commitNewComment, KeyCode.ENTER, null));
		
	}

	public void setOrder(Order order) {
		this.order = order;
		newCommentInput.setValue("");
		items.removeAllComponents();
		order.getHistory().forEach(historyItem -> {		
			items.addComponent(new Label(formatTimestamp(historyItem)));
			items.addComponent(new Label(historyItem.getCreatedBy().getName()));
			items.addComponent(new Label(formatMessage(historyItem)));
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
