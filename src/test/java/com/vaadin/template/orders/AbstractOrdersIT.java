package com.vaadin.template.orders;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.LoggerFactory;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elementsbase.AbstractElement;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class AbstractOrdersIT extends TestBenchTestCase {
	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
	}
	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

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
		assertEnabled(true, element);
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

	public static <T extends AbstractElement> T findFirstElement(Class<T> elementType) {
		WebDriver driver = CurrentDriver.get();
		return new ElementQuery<>(elementType).context(driver).first();
	}

	protected void assertEnabled(boolean expectedEnabled, TestBenchElement element) {
		if (expectedEnabled) {
			if (hasAttribute(element, "disabled")) {
				throw new AssertionError("Expected element to be enabled but it has a 'disabled' attribute");
			}
			if (hasClassName(element, "v-disabled")) {
				throw new AssertionError("Expected element to be enabled but it has a 'v-disabled' class");
			}
		} else {
			if (!hasAttribute(element, "disabled") && !hasClassName(element, "v-disabled")) {
				throw new AssertionError(
						"Expected element to be disabled but it does not have a 'disabled' attribute nor a 'v-disabled' class");
			}

		}
	}

	protected boolean hasClassName(TestBenchElement element, String className) {
		return element.getClassNames().contains(className);
	}

}
