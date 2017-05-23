package com.vaadin.template.orders.ui.view;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.OrderEditViewElement;

public class LoginIT extends AbstractOrdersIT {

	@Test
	public void userIsRedirectedToRequestedView() {
		LoginViewElement.loginAsBarista(getDriver(), LoginViewElement.APP_URL + "#!order/1");
		Assert.assertEquals("#1", $(OrderEditViewElement.class).first().getOrderId().getText());
	}

	@Test
	public void logoutWorks() {
		LoginViewElement.loginAsBarista(getDriver());
		$(MenuElement.class).first().logout();
		Assert.assertEquals("Email", findElement(By.id("login-label")).getText());
	}

}
