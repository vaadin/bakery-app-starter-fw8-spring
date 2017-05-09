package com.vaadin.template.orders.ui.view.orders.object;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.ui.view.object.AbstractViewObject;
import com.vaadin.template.orders.ui.view.orders.OrderHistory;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

public class OrderEditViewObject extends AbstractViewObject {

	public void setState(OrderState state) {
		getSetStateButton().click();
		ButtonElement stateButton = $(WindowElement.class).first().$(ButtonElement.class).id(state.name());
		stateButton.click();

	}

	private ButtonElement getSetStateButton() {
		return $(ButtonElement.class).id("setState");
	}

	public OrderState getState() {
		String displayName = $(LabelElement.class).id("state").getText();
		return OrderState.forDisplayName(displayName);
	}

	public List<HistoryItemObject> getItems() {
		List<HistoryItemObject> history = new ArrayList<>();
		GridLayoutElement layout = $(GridLayoutElement.class).id("items");
		for (int i = 0; i < layout.getRowCount(); i++) {
			String date = layout.getCell(i, 0).getText();
			String message = layout.getCell(i, 1).getText();
			String author = layout.getCell(i, 1).getText();
			history.add(new HistoryItemObject(date, message, author));
		}
		return history;
	}

	public static class HistoryItemObject {

		private String date;
		private String message;
		private String author;

		public HistoryItemObject(String date, String message, String author) {
			this.date = date;
			this.message = message;
			this.author = author;
		}

		public String getDate() {
			return date;
		}

		public String getMessage() {
			return message;
		}

		public String getAuthor() {
			return author;
		}

	}

	private void scrollIntoView(WebElement element) {
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView()", element);
	}

	public void addHistoryComment(String message) {
		scrollIntoView(getAddHistoryComment());
		getAddHistoryComment().click();
		TextFieldElement input = getHistoryCommentInput();
		input.setValue(message);

		getHistorySaveComment().click();
	}

	private TextFieldElement getHistoryCommentInput() {
		return $(TextFieldElement.class).id(OrderHistory.COMMENT_INPUT);
	}

	private ButtonElement getHistorySaveComment() {
		return $(ButtonElement.class).id(OrderHistory.SAVE_COMMENT);
	}

	private ButtonElement getAddHistoryComment() {
		return $(ButtonElement.class).id("addComment");
	}
}
