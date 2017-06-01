package com.vaadin.template.orders.ui.view.orderedit;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.vaadin.data.ValueContext;
import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.ui.components.ConfirmationDialogDesignElement;
import com.vaadin.template.orders.ui.util.DollarPriceConverter;
import com.vaadin.template.orders.ui.view.MenuElement;
import com.vaadin.template.orders.ui.view.orderedit.OrderEditViewElement.OrderInfo;
import com.vaadin.template.orders.ui.view.orderedit.ProductInfoElement.ProductOrderData;
import com.vaadin.template.orders.ui.view.storefront.StorefrontViewElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class UpdateOrderIT extends AbstractOrdersIT {

	@Test
	public void addHistoryComment() throws IOException {
		StorefrontViewElement storeFront = loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(1);

		OrderHistoryElement history = orderEdit.getHistory();
		int initialSize = history.getHistoryItems().size();
		String message = "foo";
		String expectedAuthor = "Malin";

		history.addComment(message);
		List<OrderHistoryItemObject> items = history.getHistoryItems();
		Assert.assertEquals(initialSize + 1, items.size());

		OrderHistoryItemObject lastItem = items.get(initialSize);
		Assert.assertEquals(message, lastItem.getMessage());
		Assert.assertEquals(expectedAuthor, lastItem.getAuthor());
		assertWithinLastFiveMinutes(lastItem.getDate());

		// Refresh and verify comment was persisted
		getDriver().navigate().refresh();
		testBench().waitForVaadin();
		orderEdit = new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();

		history = orderEdit.getHistory();
		items = history.getHistoryItems();
		Assert.assertEquals(initialSize + 1, items.size());
		lastItem = items.get(initialSize);
		Assert.assertEquals(message, lastItem.getMessage());
		Assert.assertEquals(expectedAuthor, lastItem.getAuthor());
		assertWithinLastFiveMinutes(lastItem.getDate());
	}

	private void assertWithinLastFiveMinutes(String date) {
		LocalDateTime commentTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("M/d/uu h:m a"));
		Assert.assertTrue("Time (" + commentTime + ") should be within last 5 minutes",
				commentTime.until(LocalDateTime.now(), ChronoUnit.MINUTES) <= 5);

	}

	@Test
	public void updateOrderInfo() {
		StorefrontViewElement storeFront = loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(1);

		OrderState oldState = OrderState.forDisplayName(orderEdit.getStateLabel().getText());
		ElementUtil.click(orderEdit.getEditOrCancel());
		Assert.assertEquals("Cancel button has wrong caption", "Cancel",
				ElementUtil.getCaption(orderEdit.getEditOrCancel()));
		Assert.assertEquals("Save button has wrong caption", "Save", ElementUtil.getCaption(orderEdit.getOk()));

		OrderInfo currentOrder = orderEdit.getOrderInfo();
		OrderInfo updatedOrder = new OrderInfo();

		int nextStateIndex = (oldState.ordinal() + 1) % OrderState.values().length;
		OrderState newState = OrderState.values()[nextStateIndex];
		updatedOrder.state = newState;
		orderEdit.getState().selectByText(updatedOrder.state.getDisplayName());
		Customer currentCustomer = currentOrder.customer;
		Customer updatedCustomer = new Customer();

		updatedCustomer.setFullName(currentCustomer.getFullName() + "-updated");
		updatedCustomer.setPhoneNumber(currentCustomer.getPhoneNumber() + "-updated");
		updatedCustomer.setDetails(currentCustomer.getDetails() + "-updated");
		updatedOrder.customer = updatedCustomer;
		orderEdit.setCustomerInfo(updatedCustomer);

		updatedOrder.pickupLocation = "Store".equals(currentOrder.pickupLocation) ? "Bakery" : "Store";
		orderEdit.getPickupLocation().selectByText(updatedOrder.pickupLocation);
		updatedOrder.products = new ArrayList<>();
		for (int i = 0; i < currentOrder.products.size(); i++) {
			ProductOrderData updatedProduct = new ProductOrderData();
			updatedOrder.products.add(updatedProduct);
			ProductOrderData currentProduct = currentOrder.products.get(i);
			updatedProduct.setComment(currentProduct.getComment() + "-updated");
			updatedProduct.setQuantity(currentProduct.getQuantity() + 1);
			// Product is intentionally kept the same as we do not know what
			// products there are in the DB
			updatedProduct.setProduct(currentProduct.getProduct());
			updatedProduct.setPrice(currentProduct.getPrice());
		}

		orderEdit.setProducts(updatedOrder.products);

		int updatedTotal = 0;
		for (ProductOrderData data : updatedOrder.products) {
			updatedTotal += data.getQuantity() * data.getPrice();
		}
		NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		DollarPriceConverter convert = new DollarPriceConverter();
		updatedOrder.total = convert.convertToPresentation(updatedTotal, new ValueContext(Locale.US));

		ElementUtil.click(orderEdit.getOk());
		Assert.assertEquals("Save failed", "Edit", ElementUtil.getCaption(orderEdit.getEditOrCancel()));
		orderEdit.assertOrder(updatedOrder);
	}

	@Test
	public void updateButCancel() {
		StorefrontViewElement storeFront = loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(1);
		ElementUtil.click(orderEdit.getEditOrCancel());
		OrderInfo currentOrder = orderEdit.getOrderInfo();

		Customer currentCustomer = currentOrder.customer;
		Customer updatedCustomer = new Customer();

		updatedCustomer.setFullName(currentCustomer.getFullName() + "-updated");
		updatedCustomer.setDetails(currentCustomer.getDetails() + "-updated");
		updatedCustomer.setPhoneNumber(currentCustomer.getPhoneNumber() + "-updated");
		orderEdit.setCustomerInfo(updatedCustomer);

		String updatedPickupLocation = "Store".equals(currentOrder.pickupLocation) ? "Bakery" : "Store";
		orderEdit.getPickupLocation().selectByText(updatedPickupLocation);
		List<ProductOrderData> products = new ArrayList<>();
		for (int i = 0; i < currentOrder.products.size(); i++) {
			ProductOrderData updatedProduct = new ProductOrderData();
			products.add(updatedProduct);
			ProductOrderData currentProduct = currentOrder.products.get(i);
			updatedProduct.setComment(currentProduct.getComment() + "-updated");
			updatedProduct.setQuantity(currentProduct.getQuantity() + 1);
			// Product is intentionally kept the same as we do not know what
			// products there are in the DB
			updatedProduct.setProduct(currentProduct.getProduct());
			updatedProduct.setPrice(currentProduct.getPrice());
		}

		orderEdit.setProducts(products);

		ElementUtil.click(orderEdit.getEditOrCancel());
		orderEdit.assertOrder(currentOrder);
	}

	@Test
	public void emptyProductRowsDoNotPreventSave() {
		StorefrontViewElement storeFront = loginAsBarista();
		OrderEditViewElement orderEdit = storeFront.selectOrder(1);
		ElementUtil.click(orderEdit.getEditOrCancel()); // "Edit"

		int nrProducts = orderEdit.getNumberOfProducts();
		for (int i = 0; i < 3; i++) {
			ElementUtil.click(orderEdit.getAddItems());
		}
		Assert.assertEquals(nrProducts + 3, orderEdit.getNumberOfProducts());
		ElementUtil.click(orderEdit.getOk());

		// Assert saved
		Assert.assertEquals("Save failed", "Edit", ElementUtil.getCaption(orderEdit.getEditOrCancel()));

		// Should still have the same products
		Assert.assertEquals(nrProducts, orderEdit.getNumberOfProducts());
	}

	@Test
	public void confirmDialogAfterCustomerChanges() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.selectOrder(2);
		ElementUtil.click(orderEditView.getEditOrCancel());

		TextFieldElement fullName = orderEditView.getFullName();
		fullName.setValue(fullName.getValue() + "foo");

		assertConfirmationDialogBlocksLeaving(orderEditView);
	}

	@Test
	public void confirmDialogAfterProductChanges() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.selectOrder(2);
		ElementUtil.click(orderEditView.getEditOrCancel());

		TextFieldElement quantity = orderEditView.getProductInfo(0).getQuantity();
		quantity.setValue(String.valueOf(Integer.parseInt(quantity.getValue()) + 1));

		assertConfirmationDialogBlocksLeaving(orderEditView);

	}

	@Test
	public void confirmDialogAfterProductAdd() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.selectOrder(2);
		ElementUtil.click(orderEditView.getEditOrCancel());
		ElementUtil.click(orderEditView.getAddItems());

		ProductInfoElement productInfo = orderEditView.getProductInfo(orderEditView.getNumberOfProducts() - 1);
		productInfo.getProduct().selectByText("Blueberry Cheese Cake");

		assertConfirmationDialogBlocksLeaving(orderEditView);
	}

	@Test
	public void confirmDialogAfterProductDelete() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.selectOrder(2);
		ElementUtil.click(orderEditView.getEditOrCancel());

		ProductInfoElement productInfo = orderEditView.getProductInfo(0);
		productInfo.getDelete().click();

		assertConfirmationDialogBlocksLeaving(orderEditView);
	}

	private void assertConfirmationDialogBlocksLeaving(OrderEditViewElement view) {
		// Navigate away to another view
		$(MenuElement.class).first().getMenuLink("Storefront").click();
		Assert.assertTrue(view.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Logout
		$(MenuElement.class).first().logout();
		Assert.assertTrue(view.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();
	}

	@Test
	public void concurrentEditing() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.selectOrder(0);
		ElementUtil.click(orderEditView.getEditOrCancel());
		TextFieldElement fullName = orderEditView.getFullName();
		fullName.setValue(fullName.getValue() + "-edited-by-user-1");

		WebDriver otherUser = createDriver();
		try {
			openLoginView(otherUser, APP_URL).login("baker@vaadin.com", "baker");

			StorefrontViewElement otherUserOrderListView = new ElementQuery<>(StorefrontViewElement.class)
					.context(otherUser).first();
			OrderEditViewElement otherUserOrderEditView = otherUserOrderListView.selectOrder(0);
			ElementUtil.click(otherUserOrderEditView.getEditOrCancel());
			TextFieldElement otherUserFullName = otherUserOrderEditView.getFullName();
			String newValue = otherUserFullName.getValue() + "-edited-by-user-2";
			otherUserFullName.setValue(newValue);
			ElementUtil.click(otherUserOrderEditView.getOk());

			// Ensure that the changes were saved
			assertEnabledWithCaption("Edit", otherUserOrderEditView.getEditOrCancel());
			Assert.assertEquals(newValue, otherUserFullName.getValue());

			// Switch back to user 1 and try to save -> assert we are still in
			// edit mode
			ElementUtil.click(orderEditView.getOk());
			assertEnabledWithCaption("Cancel", orderEditView.getEditOrCancel());
			Assert.assertEquals(1, $(NotificationElement.class).all().size());
		} finally {
			otherUser.close();
		}
	}
}
