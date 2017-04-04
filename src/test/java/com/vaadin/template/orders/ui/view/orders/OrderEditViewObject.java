package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.ui.view.AbstractViewObject;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.WindowElement;

public class OrderEditViewObject extends AbstractViewObject {

    public void setState(OrderState state) {
        getSetStateButton().click();
        ButtonElement stateButton = $(WindowElement.class).first()
                .$(ButtonElement.class).id(state.name());
        stateButton.click();

    }

    private ButtonElement getSetStateButton() {
        return $(ButtonElement.class).id("setState");
    }

    public OrderState getState() {
        String displayName = $(LabelElement.class).id("state").getText();
        return OrderState.forDisplayName(displayName);
    }

}
