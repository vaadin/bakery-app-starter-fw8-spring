package com.vaadin.template.orders.ui.view.dashboard;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class BoardBox extends CssLayout {

    public BoardBox(Component component) {
        addStyleName("board-box");
        CssLayout inner = new CssLayout();
        inner.addStyleName("board-box-inner");
        inner.addComponent(component);
        addComponent(inner);
    }
}
