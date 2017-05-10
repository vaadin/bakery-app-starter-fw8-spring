package com.vaadin.template.orders.ui.view.orders;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.ui.view.object.LoginViewObject;
import com.vaadin.template.orders.ui.view.orders.object.OrderListViewObject;

public class UpdateOrderIT extends AbstractOrdersIT {

	@Test
	public void updateOrderState() throws IOException {
		OrderListViewObject storeFront = LoginViewObject.loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(0);

		Assert.assertEquals(OrderState.READY_FOR_PICKUP, orderEdit.getCurrentState());
		orderEdit.setState(OrderState.DELIVERED);
		Assert.assertEquals(OrderState.DELIVERED, orderEdit.getCurrentState());
	}

	@Test
	public void addHistoryComment() throws IOException {
		OrderListViewObject storeFront = LoginViewObject.loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(1);

		OrderHistoryElement history = orderEdit.getHistory();
		Assert.assertEquals(3, history.getHistoryItems().size());
		history.addComment("foo");
		List<OrderHistoryItemObject> items = history.getHistoryItems();
		Assert.assertEquals(4, items.size());
		Assert.assertEquals("foo", items.get(3).getMessage());

	}

}
