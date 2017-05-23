package com.vaadin.template.orders.ui.view.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.dashboard.DashboardViewElement;
import com.vaadin.template.orders.ui.view.orders.OrdersListViewElement;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends TestBenchElement {

	public static final String APP_URL = "http://localhost:8080/";

	public static LoginViewElement open(TestBenchDriverProxy driver, String url) {
		driver.get(url);
		TestBenchElement body = (TestBenchElement) driver.findElement(By.tagName("body"));
		return TestBench.createElement(LoginViewElement.class, body.getWrappedElement(), driver.getCommandExecutor());
	}

	public static OrdersListViewElement loginAsBarista(TestBenchDriverProxy driver) {
		LoginViewElement loginView = open(driver, APP_URL);
		loginView.login("barista@vaadin.com", "barista");
		return AbstractOrdersIT.findFirstElement(loginView, OrdersListViewElement.class);
	}

	public static void loginAsBarista(TestBenchDriverProxy driver, String url) {
		open(driver, url).login("barista@vaadin.com", "barista");
	}

	public static DashboardViewElement loginAsAdmin(TestBenchDriverProxy driver) {
		open(driver, APP_URL).login("admin@vaadin.com", "admin");
		return AbstractOrdersIT.findFirstElement(driver, DashboardViewElement.class);
	}

	private void login(String username, String password) {
		WebElement loginElement = getLogin();
		WebElement passwordElement = getPassword();
		loginElement.clear();
		loginElement.sendKeys(username);
		passwordElement.clear();
		passwordElement.sendKeys(password);

		getSubmit().click();

		waitUntilElementPresent("Login failed", By.className("navigation-bar"));
	}

	protected void waitUntilElementPresent(String errorMessage, By by) {
		new WebDriverWait(getDriver(), 30).until(driver -> !driver.findElements(by).isEmpty());

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
