package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.template.orders.ui.components.ProductComboBox;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

/**
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed, e.g
 * class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ProductInfoDesign extends CssLayout {
	protected Button delete;
	protected ProductComboBox product;
	protected TextField quantity;
	protected Label price;
	protected TextArea comment;

	public ProductInfoDesign() {
		Design.read(this);
	}
}
