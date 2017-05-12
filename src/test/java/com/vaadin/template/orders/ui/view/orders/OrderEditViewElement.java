package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.openqa.selenium.By;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.ui.view.orders.ProductInfoElement.ProductOrderData;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.WindowElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.vaadin.template.orders.ui.view.orders.OrderEditView")
public class OrderEditViewElement extends OrderEditViewDesignElement {

	public static class OrderInfo {
		LocalDate dueDate;
		LocalTime dueTime;
		Customer customer;
		String pickupLocation;
		List<ProductOrderData> products;
		String total;	
	}

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

	public int getNumberOfProducts() {
		return getProductInfoContainer().findElements(By.xpath("./div")).size();
	}

	public ProductInfoElement getProductInfo(int i) {
		return getProductInfoContainer().$(GridLayoutElement.class).get(i).wrap(ProductInfoElement.class);
	}

	public void setCustomerInfo(Customer customer) {
		getFullName().setValue(customer.getFullName());
		getPhone().setValue(customer.getPhoneNumber());
		getDetails().setValue(customer.getDetails());
	}

	public Customer getCustomerInfo() {
		Customer customer = new Customer();
		customer.setFullName(getFullName().getValue());
		customer.setPhoneNumber(getPhone().getValue());
		customer.setDetails(getDetails().getValue());
		return customer;
	}

	public OrderInfo getOrderInfo() {
		ElementUtil.scrollIntoView(getDueDate());

		OrderInfo order = new OrderInfo();
		order.customer = getCustomerInfo();
		// Disabled until https://github.com/vaadin/framework/pull/9263 is
		// merged
		// order.dueDate = getDueDate().getValue();
		order.pickupLocation = getPickupLocation().getValue();
		order.products = new ArrayList<>();
		int nrProducts = getNumberOfProducts();
		for (int i = 0; i < nrProducts; i++) {
			order.products.add(getProductInfo(i).getProductOrderData());
		}
		order.total = getTotal().getText();
		return order;
	}

	public void assertOrder(OrderInfo order) {
		OrderInfo currentInfo = getOrderInfo();
		// Disabled until https://github.com/vaadin/framework/pull/9263 is
		// merged
		// Assert.assertEquals(order.dueDate, currentInfo.dueDate);
		Assert.assertEquals(order.pickupLocation, currentInfo.pickupLocation);
		Assert.assertThat(order.customer, SamePropertyValuesAs.samePropertyValuesAs(currentInfo.customer));

		for (int i = 0; i < order.products.size(); i++) {
			Assert.assertThat(order.products.get(i),
					SamePropertyValuesAs.samePropertyValuesAs(currentInfo.products.get(i)));
		}

		// Check total sum
		Assert.assertEquals(order.total, currentInfo.total);

	}

	public void setProducts(List<ProductOrderData> products) {
		for (int i = 0; i < products.size(); i++) {
			ProductOrderData product = products.get(i);

			getProductInfo(i).setProduct(product);
		}
	}

}