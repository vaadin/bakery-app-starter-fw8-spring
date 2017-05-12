package com.vaadin.template.orders.ui;

import com.vaadin.template.orders.AbstractOrdersIT;

public class MainViewElement extends MainViewDesignElement {

	public static MainViewElement get() {
		return AbstractOrdersIT.findFirstElement(MainViewElement.class);
	}
}