package com.vaadin.template.orders;

import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.LoggerFactory;

import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;

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

}
