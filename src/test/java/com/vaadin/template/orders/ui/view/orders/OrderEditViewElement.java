package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.openqa.selenium.By;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.ui.view.orders.ProductInfoElement.ProductOrderData;
import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.elements.CssLayoutElement;
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
		OrderState state;
	}

	public OrderState getCurrentState() {
		String displayName = ElementUtil.getText(getStateLabel());
		return OrderState.forDisplayName(displayName);
	}

	public int getNumberOfProducts() {
		return getProductInfoContainer().findElements(By.xpath("./div")).size();
	}

	public ProductInfoElement getProductInfo(int i) {
		return getProductInfoContainer().$(CssLayoutElement.class).get(i).wrap(ProductInfoElement.class);
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

	public static OrderEditViewElement get(HasDriver hasDriver) {
		return AbstractOrdersIT.findFirstElement(hasDriver, OrderEditViewElement.class);
	}

}