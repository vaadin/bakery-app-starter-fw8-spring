package com.vaadin.template.orders.ui.view.orders;

import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBenchElement;

public class ElementUtil {

	private ElementUtil() {
		// Util methods only
	}

	public static void click(WebElement element) {
		scrollIntoView((TestBenchElement) element);
		element.click();
	}

	public static String getText(WebElement element) {
		scrollIntoView((TestBenchElement) element);
		return element.getText();
	}

	public static void scrollIntoView(TestBenchElement element) {
		element.getCommandExecutor().executeScript("arguments[0].scrollIntoView()", element);
	}

}
