package com.vaadin.template.orders.ui.view.orders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.app.DollarPriceConverter;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.ui.components.ConfirmationDialogDesignElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.OrderEditViewElement.OrderInfo;
import com.vaadin.template.orders.ui.view.orders.ProductInfoElement.ProductOrderData;
import com.vaadin.testbench.ElementQuery;

public class AddOrderIT extends AbstractOrdersIT {

	@Test
	public void emptyAddOrderView() {
		OrdersListViewElement ordersList = LoginViewElement.loginAsBarista(getDriver());
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();
		assertNotFound("Order state should not be shown", () -> orderEditView.getOrderState());
		assertNotFound("Order id should not be shown", () -> orderEditView.getOrderId());
		assertNotFound("Set state button should not be shown", () -> orderEditView.getSetState());

		assertEnabledWithText("Cancel", orderEditView.getCancel());
		assertEnabledWithText("Done", orderEditView.getOk());
		assertEnabledWithText("Add item", orderEditView.getAddItems());
	}

	public static class TestOrder extends OrderInfo {
		public TestOrder() {
			dueDate = LocalDate.of(2025, 12, 5);
			dueTime = LocalTime.of(8, 00);
			customer = new Customer();
			pickupLocation = "Store";
			products = new ArrayList<>();
			customer.setFullName("First Last");
			customer.setPhoneNumber("Phone");
			customer.setDetails("Details");

			ProductOrderData productOrderData = new ProductOrderData("Bacon Salami Tart", 2, "Lactose free");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice(79.05);
			products.add(productOrderData);
			productOrderData = new ProductOrderData("Bacon Cracker", 1, "");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice(98.77);
			products.add(productOrderData);
			total = "$256.87"; // 79.05*2+98.77
		}
	}

	@Test
	public void addOrder() {
		OrdersListViewElement ordersList = LoginViewElement.loginAsBarista(getDriver());
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();

		OrderInfo testOrder = new TestOrder();
		orderEditView.setCustomerInfo(testOrder.customer);
		orderEditView.getDueDate().setValue(format(testOrder.dueDate));
		orderEditView.getDueTime().selectByText(testOrder.dueTime.toString());
		orderEditView.getPickupLocation().selectByText(testOrder.pickupLocation);

		for (int i = 0; i < testOrder.products.size(); i++) {
			if (i > 0) {
				ElementUtil.click(orderEditView.getAddItems());
			}
			ProductOrderData product = testOrder.products.get(i);
			ProductInfoElement productInfo = orderEditView.getProductInfo(i);
			productInfo.setProduct(product);
			// Check that (unit) price was updated correctly
			String itemPriceText = ElementUtil.getText(productInfo.getPrice());
			Result<Integer> itemPrice = new DollarPriceConverter().convertToModel(itemPriceText,
					new ValueContext(Locale.US));

			Assert.assertEquals((Integer) product.getPrice(), itemPrice.getOrThrow(RuntimeException::new));
		}

		// Check total sum
		Assert.assertEquals(testOrder.total, ElementUtil.getText(orderEditView.getTotal()));

		// Add empty row
		ElementUtil.click(orderEditView.getAddItems());

		// Add + delete row
		ElementUtil.click(orderEditView.getAddItems());
		ProductOrderData product = testOrder.products.get(0);
		ProductInfoElement productInfo = orderEditView.getProductInfo(orderEditView.getNumberOfProducts() - 1);
		productInfo.setProduct(product);
		Assert.assertEquals(testOrder.products.size() + 2, orderEditView.getNumberOfProducts());
		ElementUtil.click(orderEditView.getProductInfo(orderEditView.getNumberOfProducts() - 1).getDelete());

		// One extra row at the bottom, but it should not affect the result
		Assert.assertEquals(testOrder.products.size() + 1, orderEditView.getNumberOfProducts());

		// Done -> go to confirmation screen
		ElementUtil.click(orderEditView.getOk());
		// Ensure that that we are on the confirmation screen
		Assert.assertEquals("Place order", orderEditView.getOk().getText());

		// Order info intact
		orderEditView.assertOrder(testOrder);

		// Empty rows should have been removed
		Assert.assertEquals(testOrder.products.size(), orderEditView.getNumberOfProducts());

		// Place order -> go to order report screen
		// This causes a reload so we need first wait until the refresh is done
		// and then fetch a new orderEditView reference
		ElementUtil.click(orderEditView.getOk());
		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = $(OrderEditViewElement.class).first();

		// ID is of type #1234
		String orderIdText = ElementUtil.getText(orderEditView.getOrderId());
		Assert.assertTrue(orderIdText.matches("#\\d+"));
		Assert.assertEquals(OrderState.NEW, orderEditView.getCurrentState());

		// Order info intact
		orderEditView.assertOrder(testOrder);

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
		orderEditView.assertOrder(testOrder);

	}

	@Test
	public void changeStateForNewOrder() {
		OrdersListViewElement ordersList = LoginViewElement.loginAsBarista(getDriver());
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();

		orderEditView.getFullName().setValue("fullname");
		orderEditView.getPhone().setValue("phone");
		orderEditView.getDetails().setValue("detailss");
		orderEditView
				.setProducts(Collections.singletonList(new ProductOrderData("Bacon Cheese Cake", 12, "A comment")));

		ElementUtil.click(orderEditView.getOk());
		ElementUtil.click(orderEditView.getOk());

		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = $(OrderEditViewElement.class).first();

		Assert.assertEquals(OrderState.NEW, orderEditView.getCurrentState());

		orderEditView.setState(OrderState.CONFIRMED);
		Assert.assertEquals(OrderState.CONFIRMED, orderEditView.getCurrentState());
	}

	private String format(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("M/dd/yy"));

	}

	@Test
	public void confirmDialogWhenAbandoningNewOrder() {
		OrdersListViewElement ordersList = LoginViewElement.loginAsBarista(getDriver());
		OrderEditViewElement orderEditView = ordersList.clickNewOrder();

		orderEditView.getFullName().setValue("Something");

		// Navigate away and check that we did not move away and cancel the
		// confirmation dialog

		// Navigate away to another view
		$(MenuElement.class).first().getMenuLink("Storefront").click();
		Assert.assertTrue(orderEditView.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Logout
		$(MenuElement.class).first().logout();
		Assert.assertTrue(orderEditView.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

	}

}
