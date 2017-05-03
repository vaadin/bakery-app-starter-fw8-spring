package com.vaadin.template.orders.ui.view.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.ui.view.dashboard.object.DashboardViewObject;
import com.vaadin.template.orders.ui.view.orders.object.OrderListViewObject;

public class LoginViewObject extends AbstractViewObject {

    private static final String APP_URL = "http://localhost:8080/";

    public static OrderListViewObject loginAsBarista() {
        new LoginViewObject().login("barista@vaadin.com", "barista");
        return new OrderListViewObject();
    }

    public static DashboardViewObject loginAsAdmin() {
        new LoginViewObject().login("admin@vaadin.com", "admin");
        return new DashboardViewObject();
    }

    private void login(String username, String password) {
        getDriver().get(APP_URL);
        WebElement loginElement = getLogin();
        WebElement passwordElement = getPassword();
        loginElement.clear();
        loginElement.sendKeys(username);
        passwordElement.clear();
        passwordElement.sendKeys(password);

        getSubmit().click();

        // try {
        // testBench().compareScreen("submit-clicked");
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
        waitUntilElementPresent("Login failed", By.className("navigation-bar"));
        // Assert.assertFalse("Login failed",
        // isElementPresent(By.id("button-submit")));
    }

    private WebElement getSubmit() {
        return findElement(By.id("button-submit"));
    }

    private WebElement getPassword() {
        return findElement(By.id("password"));
    }

    private WebElement getLogin() {
        return findElement(By.id("login"));
    }

}
