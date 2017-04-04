package com.vaadin.template.orders.ui;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.ui.view.AbstractViewObject;
import com.vaadin.template.orders.ui.view.orders.StorefrontViewObject;
import com.vaadin.testbench.By;

public class MenuObject extends AbstractViewObject {

    public StorefrontViewObject navigateToStorefront() {
        // ../.. is because WebDriver refuses to click on a covered element
        WebElement menuLink = getMenuLink("Storefront");
        menuLink.click();
        return new StorefrontViewObject();
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
