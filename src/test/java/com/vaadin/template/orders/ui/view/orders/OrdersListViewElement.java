package com.vaadin.template.orders.ui.view.orders;

import org.openqa.selenium.NoSuchElementException;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.template.orders.ui.view.orders.OrdersListView")
public class OrdersListViewElement extends OrdersListViewDesignElement {

	public OrderEditViewElement selectOrder(int index) {
		GridElement grid = getList();
		grid.getRow(index).click();
		return OrderEditViewElement.get();
	}

	public OrderEditViewElement clickNewOrder() {
		getNewOrder().click();
		return OrderEditViewElement.get();
	}

	public static OrdersListViewElement get() {
		return AbstractOrdersIT.findFirstElement(OrdersListViewElement.class);
	}

	public boolean isOpen() {
		try {
			return get() != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}