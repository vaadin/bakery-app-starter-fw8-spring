package com.vaadin.template.orders.ui.view.object;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.orders.OrdersListViewElement;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.AbstractComponentElement;

public class MenuElement extends AbstractComponentElement {

	public OrdersListViewElement navigateToStorefront() {
		WebElement menuLink = getMenuLink("Storefront");
		menuLink.click();
		return AbstractOrdersIT.findFirstElement(OrdersListViewElement.class);
	}

	public WebElement getMenuLink(String caption) {
		try {
			// ../.. is because WebDriver refuses to click on a covered element
			return findElement(By.xpath("//span[text()='" + caption + "']/../.."));
		} catch (NoSuchElementException e) {
			return null;
		}
	}

}
