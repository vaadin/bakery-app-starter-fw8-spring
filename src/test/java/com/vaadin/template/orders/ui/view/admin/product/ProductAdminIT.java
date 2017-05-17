package com.vaadin.template.orders.ui.view.admin.product;

import com.vaadin.template.orders.ui.view.admin.AbstractCrudIT;
import com.vaadin.template.orders.ui.view.dashboard.DashboardViewElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;

public class ProductAdminIT extends AbstractCrudIT {
	@Override
	protected ProductAdminViewElement loginAndNavigateToView() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Products"));
		return ProductAdminViewElement.get();
	}

}
