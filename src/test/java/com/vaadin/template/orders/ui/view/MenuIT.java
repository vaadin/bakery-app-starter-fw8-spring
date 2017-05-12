package com.vaadin.template.orders.ui.view;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.MainViewElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;

public class MenuIT extends AbstractOrdersIT {

	@Test
	public void adminSeesAdminMenus() {
		LoginViewElement.loginAsAdmin();
		MenuElement menu = MainViewElement.get().getMenu();
		Assert.assertNotNull(menu.getMenuLink("Users"));
		Assert.assertNotNull(menu.getMenuLink("Products"));
	}

	@Test
	public void baristaDoesNotSeeAdminMenus() {
		LoginViewElement.loginAsBarista();
		MenuElement menu = MainViewElement.get().getMenu();
		Assert.assertNull(menu.getMenuLink("Users"));
		Assert.assertNull(menu.getMenuLink("Products"));
	}
}
