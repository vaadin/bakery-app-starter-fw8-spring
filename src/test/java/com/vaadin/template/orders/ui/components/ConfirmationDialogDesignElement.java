package com.vaadin.template.orders.ui.components;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.template.orders.ui.components.ConfirmationDialogDesign")
public class ConfirmationDialogDesignElement extends VerticalLayoutElement {

	public static ConfirmationDialogDesignElement get() {
		return AbstractOrdersIT.findFirstElement(ConfirmationDialogDesignElement.class);
	}

	public ButtonElement getCancel() {
		return $(ButtonElement.class).id("cancel");
	}

	public ButtonElement getOk() {
		return $(ButtonElement.class).id("ok");
	}

}
