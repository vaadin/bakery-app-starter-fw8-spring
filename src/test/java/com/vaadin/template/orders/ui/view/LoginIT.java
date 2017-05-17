package com.vaadin.template.orders.ui.view;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.orders.OrderEditViewElement;

public class LoginIT extends AbstractOrdersIT {

	@Test
	public void userIsRedirectedToRequestedView() {
		LoginViewElement.loginAsBarista(LoginViewElement.APP_URL + "#!order/1");
		OrderEditViewElement orderEditView = OrderEditViewElement.get();
		Assert.assertEquals("#1", orderEditView.getOrderId().getText());
	}
}
