package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.WindowElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.template.orders.ui.view.orders.OrderEditView")
public class OrderEditViewElement extends OrderEditViewDesignElement {
	public void setState(OrderState state) {
		getSetState().click();
		// Can't use $() here as it would try to find the Window inside the view
		// and it's attached to the body
		WindowElement windowElement = new ElementQuery<>(WindowElement.class).context(getDriver()).first();
		ButtonElement stateButton = windowElement.$(ButtonElement.class).id(state.name());
		stateButton.click();
	}

	public OrderState getCurrentState() {
		String displayName = getOrderState().getText();
		return OrderState.forDisplayName(displayName);
	}

	public ProductInfoElement getProductInfo(int i) {
		return getProductInfoContainer().$(GridLayoutElement.class).get(i).wrap(ProductInfoElement.class);
	}

	public void setCustomerInfo(Customer customer) {
		getFirstName().setValue(customer.getFirstName());
		getLastName().setValue(customer.getLastName());
		getPhone().setValue(customer.getPhoneNumber());
		getEmail().setValue(customer.getEmail());
		getDetails().setValue(customer.getDetails());
	}

	public Customer getCustomerInfo() {
		Customer customer = new Customer();
		customer.setFirstName(getFirstName().getValue());
		customer.setLastName(getLastName().getValue());
		customer.setPhoneNumber(getPhone().getValue());
		customer.setEmail(getEmail().getValue());
		customer.setDetails(getDetails().getValue());
		return customer;
	}

}