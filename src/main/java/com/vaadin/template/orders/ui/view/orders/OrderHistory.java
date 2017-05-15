package com.vaadin.template.orders.ui.view.orders;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.DateTimeFormatter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SpringComponent
@PrototypeScope
public class OrderHistory extends OrderHistoryDesign {

	public static final String COMMENT_INPUT = "commentInput";
	public static final String SAVE_COMMENT = "commitCommentButton";

	@Autowired
	private DateTimeFormatter dateTimeFormatter;

	@Autowired
	private OrderHistoryController controller;

	private final TextField newCommentInput = new TextField();
	private final Button commitNewComment = new Button("Add comment");
	private Order order;

	@PostConstruct
	public void init() {
		controller.init(this);
		newCommentInput.setId(COMMENT_INPUT);
		commitNewComment.setId(SAVE_COMMENT);
		addComment.addClickListener(e -> {
			items.addComponent(newCommentInput);
			items.addComponent(commitNewComment);
			addComment.setVisible(false);
			newCommentInput.focus();
		});

		commitNewComment.addClickListener(e -> controller.addNewComment(newCommentInput.getValue()));
	}

	public void setOrder(Order order) {
		this.order = order;
		newCommentInput.setValue("");
		addComment.setVisible(true);
		items.removeAllComponents();
		order.getHistory().forEach(historyItem -> {
			items.addComponent(new Label(formatMessage(historyItem)));
			items.addComponent(new Label(formatTimestamp(historyItem)));
			items.addComponent(new Label(historyItem.getCreatedBy().getName()));
		});
	}

	private String formatTimestamp(HistoryItem historyItem) {
		return historyItem.getMessage();
	}

	private String formatMessage(HistoryItem historyItem) {
		return dateTimeFormatter.format(historyItem.getTimestamp(), Locale.US);
	}

	public Order getOrder() {
		return order;
	}

}
