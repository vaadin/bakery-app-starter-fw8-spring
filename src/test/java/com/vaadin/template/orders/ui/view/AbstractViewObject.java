package com.vaadin.template.orders.ui.view;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.vaadin.template.orders.ui.MenuObject;
import com.vaadin.template.orders.ui.view.orders.CurrentDriver;
import com.vaadin.testbench.TestBenchTestCase;

public class AbstractViewObject extends TestBenchTestCase {

    @Override
    public WebDriver getDriver() {
        return CurrentDriver.get();
    }

    protected Object executeScript(String script, Object... args) {
        WebDriver driver = getDriver();
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) getDriver()).executeScript(script,
                    args);
        } else {
            throw new UnsupportedOperationException(
                    "The web driver does not support JavaScript execution");
        }
    }

    protected WebElement getShadowRoot(WebElement element) {
        return (WebElement) executeScript("return arguments[0].shadowRoot",
                element);
    }

    @Override
    public SearchContext getContext() {
        return getDriver();
    }

    public MenuObject getMenu() {
        dismissLicenseDialog();
        return new MenuObject();
    }

    private void dismissLicenseDialog() {
        List<WebElement> elements = findElements(
                By.tagName("vaadin-license-dialog"));
        if (elements.isEmpty()) {
            return;
        }

        WebElement dialog = elements.get(0);
        WebElement closeButton = getShadowRoot(dialog)
                .findElement(By.id("licenseDialogClose"));
        closeButton.click();
    }

    protected void waitUntilElementPresent(String errorMessage, By by) {
        new WebDriverWait(getDriver(), 30)
                .until((Predicate<WebDriver>) driver -> isElementPresent(by));

    }

}
