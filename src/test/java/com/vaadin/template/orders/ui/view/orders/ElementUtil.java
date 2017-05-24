package com.vaadin.template.orders.ui.view.orders;

import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.AbstractComponentElement;

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

	public static String getCaption(AbstractComponentElement element) {
		scrollIntoView((TestBenchElement) element);
		return element.getCaption();
	}

	public static void scrollIntoView(TestBenchElement element) {
		element.getCommandExecutor().executeScript("arguments[0].scrollIntoView()", element);
	}

}
