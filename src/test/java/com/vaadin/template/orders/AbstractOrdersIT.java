package com.vaadin.template.orders;

import java.util.function.Supplier;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.LoggerFactory;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class AbstractOrdersIT extends TestBenchTestCase {
	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
	}
	// Disabled for now because ChromeDriver sometimes seems to freeze on this
	// @Rule
	// public ScreenshotOnFailureRule screenshotOnFailure = new
	// ScreenshotOnFailureRule(this, true);

	@After
	public void destroyDriver() {
		if (getDriver() != null) {
			getDriver().quit();
		}
	}

	@Before
	public void createDriver() {
		WebDriver driver = new ChromeDriver();
		setDriver(driver);
		CurrentDriver.set(driver);
	}

	protected boolean hasAttribute(WebElement element, String name) {
		return internalGetAttribute(element, name) != null;
	}

	protected Object internalGetAttribute(WebElement element, String name) {
		return getCommandExecutor().executeScript("return arguments[0].getAttribute(arguments[1]);", element, name);
	}

	protected void assertEnabledWithText(String text, TestBenchElement element) {
		Assert.assertFalse(hasAttribute(element, "disabled"));
		Assert.assertEquals(text, ElementUtil.getText(element));
	}

	/**
	 * Assert that the element looked up by the given function is not in the
	 * DOM. If it is not, an {@link AssertionError} is thrown with the given
	 * message.
	 *
	 * @param message
	 *            the message for the {@link AssertionError}
	 * @param elementSupplier
	 *            the function which returns the element
	 */
	protected void assertNotFound(String message, Supplier<WebElement> elementSupplier) {
		try {
			elementSupplier.get();
			Assert.fail("Element");
		} catch (NoSuchElementException e) {
			// Everything ok
		}

	}

}
