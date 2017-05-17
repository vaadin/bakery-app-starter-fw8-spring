package com.vaadin.template.orders.ui.view.admin.user;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.ui.view.admin.AbstractCrudIT;
import com.vaadin.template.orders.ui.view.dashboard.DashboardViewElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class UserAdminIT extends AbstractCrudIT {

	@Override
	protected UserAdminViewElement loginAndNavigateToView() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Users"));
		return UserAdminViewElement.get();
	}

	@Test
	public void updatePassword() {
		UserAdminViewElement userAdmin = loginAndNavigateToView();
		// Change the password for baker to foo and back to baker
		TestBenchElement bakerCell = getCell(userAdmin.getList(), "baker@vaadin.com");
		bakerCell.click();

		TextFieldElement passwordField = userAdmin.getPassword();
		Assert.assertEquals("", passwordField.getValue());

		passwordField.setValue("foo");
		ButtonElement update = userAdmin.getUpdate();
		update.click();

		assertEnabled(false, userAdmin.getForm());

		bakerCell.click();
		Assert.assertEquals("", passwordField.getValue());
		passwordField.setValue("baker");
		update.click();

		assertEnabled(false, userAdmin.getForm());
	}

}
