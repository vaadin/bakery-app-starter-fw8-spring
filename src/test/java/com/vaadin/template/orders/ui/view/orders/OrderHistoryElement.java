package com.vaadin.template.orders.ui.view.orders;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.template.orders.ui.view.orders.OrderHistory")
public class OrderHistoryElement extends OrderHistoryDesignElement {

	private TextFieldElement getCommentInput() {
		return $(TextFieldElement.class).id(OrderHistory.COMMENT_INPUT);
	}

	private ButtonElement getSaveComment() {
		return $(ButtonElement.class).id(OrderHistory.SAVE_COMMENT);
	}

	public List<OrderHistoryItemObject> getHistoryItems() {
		List<OrderHistoryItemObject> history = new ArrayList<>();
		GridLayoutElement layout = getItems();
		ElementUtil.scrollIntoView(layout);
		for (int i = 0; i < layout.getRowCount(); i++) {
			String date = layout.getCell(i, 0).getText();
			String message = layout.getCell(i, 1).getText();
			String author = layout.getCell(i, 2).getText();
			history.add(new OrderHistoryItemObject(date, message, author));
		}
		return history;
	}

	public void addComment(String message) {
		ElementUtil.click(getAddComment());
		TextFieldElement input = getCommentInput();
		input.setValue(message);

		getSaveComment().click();
	}

}