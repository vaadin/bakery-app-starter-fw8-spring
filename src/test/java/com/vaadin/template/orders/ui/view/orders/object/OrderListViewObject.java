package com.vaadin.template.orders.ui.view.orders.object;

import com.vaadin.template.orders.ui.view.object.AbstractViewObject;
import com.vaadin.testbench.elements.GridElement;

public class OrderListViewObject extends AbstractViewObject {

    public OrderEditViewObject selectOrder(int index) {
        GridElement grid = getOrderGrid();
        grid.getRow(index).click();
        return new OrderEditViewObject();
    }

    public GridElement getOrderGrid() {
        return $(GridElement.class).first();
    }

}
