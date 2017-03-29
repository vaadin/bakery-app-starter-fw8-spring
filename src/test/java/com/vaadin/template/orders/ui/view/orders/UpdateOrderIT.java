package com.vaadin.template.orders.ui.view.orders;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.ui.LoginViewObject;

public class UpdateOrderIT extends AbstractOrdersIT {

    @Test
    public void updateOrderState() throws IOException {
        StorefrontViewObject storeFront = LoginViewObject.loginAsBarista();
        OrderEditViewObject orderEdit = storeFront.selectOrder(0);

        Assert.assertEquals(OrderState.READY_FOR_PICKUP, orderEdit.getState());
        orderEdit.setState(OrderState.DELIVERED);
        Assert.assertEquals(OrderState.DELIVERED, orderEdit.getState());
    }

}
