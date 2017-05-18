package com.vaadin.template.orders.ui.view.admin.product;

import org.junit.Assert;

import com.vaadin.template.orders.ui.view.admin.AbstractCrudIT;
import com.vaadin.template.orders.ui.view.dashboard.DashboardViewElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.elements.TextFieldElement;

public class ProductAdminIT extends AbstractCrudIT<ProductAdminViewElement> {
	@Override
	protected ProductAdminViewElement loginAndNavigateToView() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Products"));
		return ProductAdminViewElement.get();
	}

	@Override
	protected void assertFormFieldsEmpty(ProductAdminViewElement view) {
		Assert.assertEquals("", ElementUtil.getText(view.getPrice()));
		Assert.assertEquals("", ElementUtil.getText(view.getPrice()));
	}

	@Override
	protected void populateNewEntity(ProductAdminViewElement view) {
		view.getName().setValue("New product");
		view.getPrice().setValue("$12.34");
	}

	@Override
	protected TextFieldElement getFirstFormTextField(ProductAdminViewElement view) {
		return view.getName();
	}

}
