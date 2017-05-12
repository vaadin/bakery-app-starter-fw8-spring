package com.vaadin.template.orders.ui;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;

public class CurrentDriver {
	private static ThreadLocal<TestBenchDriverProxy> currentDriver = new ThreadLocal<>();

	public static TestBenchDriverProxy get() {
		return currentDriver.get();
	}

	public static void set(WebDriver driver) {
		if (driver != null && !(driver instanceof TestBenchDriverProxy)) {
			driver = TestBench.createDriver(driver);
		}

		currentDriver.set((TestBenchDriverProxy) driver);
	}
}
