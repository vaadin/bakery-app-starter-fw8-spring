package com.vaadin.template.orders.ui.view;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.dashboard.object.DashboardViewObject;
import com.vaadin.template.orders.ui.view.object.LoginViewObject;
import com.vaadin.template.orders.ui.view.orders.object.OrderListViewObject;

public class MenuIT extends AbstractOrdersIT {

    @Test
    public void adminSeesAdminMenus() {
        DashboardViewObject dashboard = LoginViewObject.loginAsAdmin();
        Assert.assertNotNull(dashboard.getMenu().getMenuLink("Users"));
        Assert.assertNotNull(dashboard.getMenu().getMenuLink("Products"));
    }

    @Test
    public void baristaDoesNotSeeAdminMenus() {
        OrderListViewObject storefront = LoginViewObject.loginAsBarista();
        Assert.assertNull(storefront.getMenu().getMenuLink("Users"));
        Assert.assertNull(storefront.getMenu().getMenuLink("Products"));
    }
}
