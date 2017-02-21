package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Label;
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
public class OrderViewDesign extends VerticalLayout {
    protected DateTimeField dueDateTime;
    protected ComboBox<com.vaadin.template.orders.backend.data.entity.PickupLocation> pickupLocation;
    protected TextField firstName;
    protected TextField lastName;
    protected TextField phone;
    protected TextField email;
    protected TextField details;
    protected CheckBox newCustomer;
    protected CssLayout productInfoContainer;
    protected Button addItems;
    protected Label total;
    protected Button cancel;
    protected Button done;

    public OrderViewDesign() {
        Design.read(this);
    }
}
