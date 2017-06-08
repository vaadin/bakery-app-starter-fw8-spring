package com.vaadin.starter.bakery.ui.view.storefront;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.testbench.elements.GridElement;

public class StorefrontIT extends AbstractIT {

	@Test
	public void gridContainsData() {
		StorefrontViewElement storefront = loginAsBarista();

		GridElement grid = storefront.getList();
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
		openLoginView(APP_URL + "#!storefront/search=kerry").login("barista@vaadin.com", "barista");
		StorefrontViewElement view = $(StorefrontViewElement.class).first();
		GridElement list = view.getList();
		long rowCount = list.getRowCount();
		Assert.assertTrue(rowCount > 0);
		Assert.assertTrue(rowCount < 100);
		Assert.assertTrue(list.getCell(0, 1).getText().toLowerCase().contains("kerry"));
	}

	@Test
	public void filterUsingSearchField() {
		StorefrontViewElement view = loginAsBarista();
		view.getSearchField().setValue("gallegos");
		view.getSearchButton().click();

		GridElement list = view.getList();
		long rowCount = list.getRowCount();
		Assert.assertTrue(rowCount > 0);
		Assert.assertTrue(rowCount < 100);
		Assert.assertTrue(list.getCell(0, 1).getText().toLowerCase().contains("gallegos"));
	}
}
