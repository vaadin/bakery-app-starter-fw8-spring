package com.vaadin.template.orders.ui.view.admin.product;

import org.openqa.selenium.NoSuchElementException;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.testbench.ElementQuery;

public class ProductAdminViewElement extends ProductAdminViewDesignElement implements CrudViewElement {

	public static ProductAdminViewElement get() {
		return new ElementQuery<>(ProductAdminViewElement.class).context(CurrentDriver.get()).first();
	}

	@Override
	public boolean isOpen() {
		try {
			return get() != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}