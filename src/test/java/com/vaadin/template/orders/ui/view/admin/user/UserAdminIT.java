package com.vaadin.template.orders.ui.view.admin.user;

import java.util.List;

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

public class UserAdminIT extends AbstractCrudIT<UserAdminViewElement> {

	@Override
	protected UserAdminViewElement loginAndNavigateToView() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Users"));
		return UserAdminViewElement.get();
	}

	@Override
	protected void populateNewEntity(UserAdminViewElement view) {
		view.getEmail().setValue("john@doe.com");
		view.getName().setValue("John doe");
		view.getPassword().setValue("john");
		view.getRole().selectByText("admin");
	}

	@Override
	protected TextFieldElement getFirstFormTextField(UserAdminViewElement view) {
		return view.getName();
	}

	@Override
	protected List<Integer> getUniquelySortableColumnIndexes(UserAdminViewElement view) {
		List<Integer> cols = super.getUniquelySortableColumnIndexes(view);
		cols.remove(2); // Role sorting is not stable
		return cols;
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
		assertEditState(userAdmin, false);

		bakerCell.click();
		Assert.assertEquals("", passwordField.getValue());
		passwordField.setValue("baker");
		update.click();

		assertEditState(userAdmin, false);
	}

	@Override
	protected void assertFormFieldsEmpty(UserAdminViewElement view) {
		Assert.assertEquals("", ElementUtil.getText(view.getEmail()));
		Assert.assertEquals("", ElementUtil.getText(view.getName()));
		Assert.assertEquals("", ElementUtil.getText(view.getPassword()));
		Assert.assertEquals("", view.getRole().getValue());
	}

}
