package com.vaadin.template.orders.ui.view.admin.user;

import org.openqa.selenium.NoSuchElementException;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.template.orders.ui.view.admin.product.CrudViewElement;
import com.vaadin.testbench.ElementQuery;

public class UserAdminViewElement extends UserAdminViewDesignElement implements CrudViewElement {

	public static UserAdminViewElement get() {
		return new ElementQuery<>(UserAdminViewElement.class).context(CurrentDriver.get()).first();
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