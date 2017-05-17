package com.vaadin.template.orders.ui.view.admin.product;

import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

public interface CrudViewElement {

	GridElement getList();

	TextFieldElement getSearch();

}
