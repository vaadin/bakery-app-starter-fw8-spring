package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.template.orders.ui.view.AbstractViewObject;
import com.vaadin.testbench.elements.GridElement;

public class StorefrontViewObject extends AbstractViewObject {

    public OrderEditViewObject selectOrder(int index) {
        GridElement grid = getOrderGrid();
        grid.getRow(index).click();
        return new OrderEditViewObject();
    }

    public GridElement getOrderGrid() {
        return $(GridElement.class).first();
    }

}
