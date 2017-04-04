package com.vaadin.template.orders.ui;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.ui.view.dashboard.DashboardViewObject;
import com.vaadin.template.orders.ui.view.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.orders.StorefrontViewObject;

public class MenuIT extends AbstractOrdersIT {

    @Test
    public void adminSeesAdminMenus() {
        DashboardViewObject dashboard = LoginViewObject.loginAsAdmin();
        Assert.assertNotNull(dashboard.getMenu().getMenuLink("Users"));
        Assert.assertNotNull(dashboard.getMenu().getMenuLink("Products"));
    }

    @Test
    public void baristaDoesNotSeeAdminMenus() {
        StorefrontViewObject storefront = LoginViewObject.loginAsBarista();
        Assert.assertNull(storefront.getMenu().getMenuLink("Users"));
        Assert.assertNull(storefront.getMenu().getMenuLink("Products"));
    }
}
