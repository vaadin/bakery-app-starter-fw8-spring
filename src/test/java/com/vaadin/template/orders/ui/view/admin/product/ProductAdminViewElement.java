package com.vaadin.template.orders.ui.view.admin.product;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.testbench.ElementQuery;

public class ProductAdminViewElement extends ProductAdminViewDesignElement implements CrudViewElement {

	public static ProductAdminViewElement get() {
		return new ElementQuery<>(ProductAdminViewElement.class).context(CurrentDriver.get()).first();
	}
}