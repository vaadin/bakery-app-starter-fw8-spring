package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class BulletinBoard extends CssLayout {
    protected Label messageLabel = new Label();

    public BulletinBoard() {
        setWidth("100%");
        addStyleName("bulletin-board");

        messageLabel.setContentMode(ContentMode.HTML);
        messageLabel.addStyleName("message");
        messageLabel.setWidth("100%");
        addComponent(messageLabel);
        setMessage("<b>We're out of pink sugarcoating</b>" + //
                "<br> 2016.01.13 06:13 Heidi" + //
                "<br>" + //
                "<br> Please don't accept any orders due before Friday using the pink stuff!");
    }

    public void setMessage(String message) {
        messageLabel.setValue(message);
    }
}
