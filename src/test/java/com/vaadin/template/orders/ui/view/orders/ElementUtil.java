package com.vaadin.template.orders.ui.view.orders;

import com.vaadin.testbench.TestBenchElement;

public class ElementUtil {

	private ElementUtil() {
		// Util methods only
	}

	public static void click(TestBenchElement element) {
		scrollIntoView(element);
		element.click();
	}

	public static String getText(TestBenchElement element) {
		scrollIntoView(element);
		return element.getText();
	}

	public static void scrollIntoView(TestBenchElement element) {
		element.getCommandExecutor().executeScript("arguments[0].scrollIntoView()", element);
	}

}
