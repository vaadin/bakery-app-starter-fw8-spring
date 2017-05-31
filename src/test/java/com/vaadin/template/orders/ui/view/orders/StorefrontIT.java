package com.vaadin.template.orders.ui.view.orders;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.testbench.elements.GridElement;

public class StorefrontIT extends AbstractOrdersIT {

	@Test
	public void gridContainsData() {
		OrdersListViewElement ordersList = loginAsBarista();

		GridElement grid = ordersList.getList();
		Assert.assertTrue("With the generated data, there should be at least twenty rows in the grid",
				grid.getRowCount() > 20);

		String dueCellText = grid.getCell(0, 0).getText();
		Assert.assertTrue("With the generated data, there should be at least one order due today",
				dueCellText.contains("Today"));

		String customerAndProductsText = grid.getCell(0, 1).getText();
		Assert.assertTrue("The customer and products part should contain data", customerAndProductsText.length() > 10);
	}

	@Test
	public void filterUsingUrl() {
		openLoginView(APP_URL + "#!orders-list/search=kerry").login("barista@vaadin.com", "barista");
		OrdersListViewElement view = $(OrdersListViewElement.class).first();
		GridElement list = view.getList();
		long rowCount = list.getRowCount();
		Assert.assertTrue(rowCount > 0);
		Assert.assertTrue(rowCount < 100);
		Assert.assertTrue(list.getCell(0, 1).getText().toLowerCase().contains("kerry"));
	}

	@Test
	public void filterUsingSearchField() {
		OrdersListViewElement view = loginAsBarista();
		view.getSearchField().setValue("gallegos");
		ElementUtil.click(view.getSearchButton());

		GridElement list = view.getList();
		long rowCount = list.getRowCount();
		Assert.assertTrue(rowCount > 0);
		Assert.assertTrue(rowCount < 100);
		Assert.assertTrue(list.getCell(0, 1).getText().toLowerCase().contains("gallegos"));
	}
}
