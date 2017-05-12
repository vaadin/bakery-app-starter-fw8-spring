package com.vaadin.template.orders.ui.view.orders;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.testbench.elements.GridElement;

public class StorefrontIT extends AbstractOrdersIT {

	@Test
	public void gridContainsData() {
		OrdersListViewElement ordersList = LoginViewElement.loginAsBarista();

		GridElement grid = ordersList.getList();
		Assert.assertTrue("With the generated data, there should be at least twenty rows in the grid",
				grid.getRowCount() > 20);

		String dueCellText = grid.getCell(0, 0).getText();
		Assert.assertTrue("With the generated data, there should be at least one order due today",
				dueCellText.contains("Today"));

		String customerAndProductsText = grid.getCell(0, 1).getText();
		Assert.assertTrue("The customer and products part should contain data", customerAndProductsText.length() > 10);
	}
}
