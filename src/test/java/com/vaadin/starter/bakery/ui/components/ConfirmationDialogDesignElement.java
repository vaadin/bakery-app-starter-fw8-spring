package com.vaadin.starter.bakery.ui.components;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.starter.bakery.ui.components.ConfirmationDialogDesign")
public class ConfirmationDialogDesignElement extends VerticalLayoutElement {

	public ButtonElement getCancel() {
		return $(ButtonElement.class).id("cancel");
	}

	public ButtonElement getDiscardChanges() {
		return $(ButtonElement.class).id("discardChanges");
	}

}
