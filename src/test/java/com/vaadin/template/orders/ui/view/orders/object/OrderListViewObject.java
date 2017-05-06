package com.vaadin.template.orders.ui.view.orders.object;

import com.vaadin.template.orders.ui.view.object.AbstractViewObject;
import com.vaadin.template.orders.ui.view.orders.OrderEditViewElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;

public class OrderListViewObject extends AbstractViewObject {

	public OrderEditViewElement selectOrder(int index) {
		GridElement grid = getOrderGrid();
		grid.getRow(index).click();
		return $(OrderEditViewElement.class).first();
	}

	public GridElement getOrderGrid() {
		return $(GridElement.class).first();
	}

	public OrderEditViewElement clickNewOrder() {
		$(ButtonElement.class).id("newOrder").click();
		return $(OrderEditViewElement.class).first();
	}

}
