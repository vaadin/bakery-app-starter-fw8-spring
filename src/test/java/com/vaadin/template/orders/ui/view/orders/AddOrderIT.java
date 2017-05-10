package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.ui.view.object.LoginViewObject;
import com.vaadin.template.orders.ui.view.orders.ProductInfoElement.ProductOrderData;
import com.vaadin.template.orders.ui.view.orders.object.OrderListViewObject;
import com.vaadin.testbench.ElementQuery;

public class AddOrderIT extends AbstractOrdersIT {

	@Test
	public void emptyAddOrderView() {
		OrderListViewObject ordersList = LoginViewObject.loginAsBarista();
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();
		assertNotFound("Order state should not be shown", () -> orderEditView.getOrderState());
		assertNotFound("Order id should not be shown", () -> orderEditView.getOrderId());
		assertNotFound("Set state button should not be shown", () -> orderEditView.getSetState());

		assertEnabledWithText("Cancel", orderEditView.getCancel());
		assertEnabledWithText("Done", orderEditView.getOk());
		assertEnabledWithText("Add item", orderEditView.getAddItems());
	}

	public static class TestOrder {
		LocalDate dueDate = LocalDate.of(2025, 12, 5);
		Customer customer = new Customer();
		String pickupLocation = "Store";
		List<ProductOrderData> products = new ArrayList<>();
		String total;

		public TestOrder() {
			customer.setFirstName("First");
			customer.setLastName("Last");
			customer.setPhoneNumber("Phone");
			customer.setEmail("Email");
			customer.setDetails("Details");

			ProductOrderData productOrderData = new ProductOrderData("Bacon Salami Tart", 2, "Lactose free");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice("$79.05");
			products.add(productOrderData);
			productOrderData = new ProductOrderData("Bacon Cracker", 1, "");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice("$98.78");
			products.add(productOrderData);
			total = "$256.88"; // 79.05*2+98.78
		}

	}

	@Test
	public void addOrder() {
		OrderListViewObject ordersList = LoginViewObject.loginAsBarista();
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();

		TestOrder testOrder = new TestOrder();
		orderEditView.setCustomerInfo(testOrder.customer);
		orderEditView.getDueDate().setValue(format(testOrder.dueDate));
		orderEditView.getPickupLocation().selectByText(testOrder.pickupLocation);

		for (int i = 0; i < testOrder.products.size(); i++) {
			if (i > 0) {
				ElementUtil.click(orderEditView.getAddItems());
			}
			ProductOrderData product = testOrder.products.get(i);
			ProductInfoElement productInfo = orderEditView.getProductInfo(i);
			productInfo.setProduct(product);
			// Check that (unit) price was updated correctly
			Assert.assertEquals(product.getPrice(), ElementUtil.getText(productInfo.getPrice()));
		}

		// Check total sum
		Assert.assertEquals(testOrder.total, ElementUtil.getText(orderEditView.getTotal()));

		// Done -> go to confirmation screen
		ElementUtil.click(orderEditView.getOk());
		// Ensure that that we are on the confirmation screen
		Assert.assertEquals("Place order", orderEditView.getOk().getText());

		// Order info intact
		assertOrder(testOrder, orderEditView);

		// Place order -> go to order report screen
		// This causes a reload so we need first wait until the refresh is done
		// and then fetch a new orderEditView reference
		ElementUtil.click(orderEditView.getOk());
		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();

		// ID is of type #1234
		String orderIdText = ElementUtil.getText(orderEditView.getOrderId());
		Assert.assertTrue(orderIdText.matches("#\\d+"));
		Assert.assertEquals(OrderState.NEW, orderEditView.getCurrentState());

		// Order info intact
		assertOrder(testOrder, orderEditView);

		// It's now possible to set state
		Assert.assertNotNull(orderEditView.getSetState());

		// Check URL is update correctly so we can refresh to show the same
		// order
		int orderId = Integer.parseInt(orderIdText.substring(1));
		String url = getDriver().getCurrentUrl();
		Assert.assertTrue("Url " + url + " should end with #!order/" + orderId, url.endsWith("#!order/" + orderId));

		Assert.assertEquals("Edit", ElementUtil.getText(orderEditView.getCancel()));
		Assert.assertEquals("Confirmed", ElementUtil.getText(orderEditView.getOk()));

		// Reload and verify the order was stored in DB and shown correctly
		getDriver().navigate().refresh();
		testBench().waitForVaadin();

		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();
		assertOrder(testOrder, orderEditView);

	}

	private void assertOrder(TestOrder testOrder, OrderEditViewElement orderEditView) {
		ElementUtil.scrollIntoView(orderEditView.getDueDate());
		// Disabled until https://github.com/vaadin/framework/pull/9263 is
		// merged
		// Assert.assertEquals(format(testOrder.dueDate),
		// orderEditView.getDueDate().getValue());
		Assert.assertEquals(testOrder.pickupLocation, orderEditView.getPickupLocation().getValue());
		Assert.assertThat(testOrder.customer,
				SamePropertyValuesAs.samePropertyValuesAs(orderEditView.getCustomerInfo()));

		for (int i = 0; i < testOrder.products.size(); i++) {
			ProductOrderData productInTestOrder = testOrder.products.get(i);
			ProductInfoElement productInfo = orderEditView.getProductInfo(i);

			Assert.assertThat(productInfo.getProductOrderData(),
					SamePropertyValuesAs.samePropertyValuesAs(productInTestOrder));
		}

		// Check total sum
		Assert.assertEquals(testOrder.total, ElementUtil.getText(orderEditView.getTotal()));

	}

	private String format(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("M/dd/yy"));

	}

}