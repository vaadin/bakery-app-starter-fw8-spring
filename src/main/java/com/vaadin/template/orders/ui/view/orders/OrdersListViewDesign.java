package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.template.orders.ui.components.OrdersGrid;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class OrdersListViewDesign extends VerticalLayout {
	protected Panel searchPanel;
	protected TextField searchField;
	protected Button searchButton;
	protected CheckBox includePast;
	protected Button newOrder;
	protected OrdersGrid list;

	public OrdersListViewDesign() {
		Design.read(this);
	}
}
