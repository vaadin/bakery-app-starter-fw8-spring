package com.vaadin.template.orders.ui.view.object;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.ui.view.orders.object.OrderListViewObject;
import com.vaadin.testbench.By;

public class MenuObject extends AbstractViewObject {

    public OrderListViewObject navigateToStorefront() {
        // ../.. is because WebDriver refuses to click on a covered element
        WebElement menuLink = getMenuLink("Storefront");
        menuLink.click();
        return new OrderListViewObject();
    }

    public WebElement getMenuLink(String caption) {
        try {
            return findElement(
                    By.xpath("//span[text()='" + caption + "']/../.."));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
