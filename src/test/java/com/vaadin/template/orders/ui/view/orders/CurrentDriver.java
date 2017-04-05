package com.vaadin.template.orders.ui.view.orders;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;

public class CurrentDriver {
    private static ThreadLocal<WebDriver> currentDriver = new ThreadLocal<>();

    public static WebDriver get() {
        return currentDriver.get();
    }

    public static void set(WebDriver driver) {
        if (!(driver instanceof TestBenchDriverProxy)) {
            driver = TestBench.createDriver(driver);
        }

        currentDriver.set(driver);
    }
}