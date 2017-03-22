package com.vaadin.template.orders.ui.view.orders;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.ui.LoginViewObject;
import com.vaadin.template.orders.ui.view.orders.OrderEditViewObject.HistoryItemObject;

public class UpdateOrderIT extends AbstractOrdersIT {

    @Test
    public void updateOrderState() throws IOException {
        StorefrontViewObject storeFront = LoginViewObject.loginAsBarista();
        OrderEditViewObject orderEdit = storeFront.selectOrder(0);

        Assert.assertEquals(OrderState.READY_FOR_PICKUP, orderEdit.getState());
        orderEdit.setState(OrderState.DELIVERED);
        Assert.assertEquals(OrderState.DELIVERED, orderEdit.getState());
    }

    @Test
    public void addHistoryComment() throws IOException {
        StorefrontViewObject storeFront = LoginViewObject.loginAsBarista();
        OrderEditViewObject orderEdit = storeFront.selectOrder(1);

        Assert.assertEquals(3, orderEdit.getHistory().size());
        orderEdit.addHistoryComment("foo");
        List<HistoryItemObject> history = orderEdit.getHistory();
        Assert.assertEquals(4, history.size());
        Assert.assertEquals("foo", history.get(3).getMessage());

    }

}
