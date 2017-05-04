package com.vaadin.template.orders;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Rule;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.RequestHeaders;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.UserAgent;
import com.vaadin.template.orders.ui.CurrentDriver;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;

public class AbstractOrdersIT extends TestBenchTestCase {

	{
		// Workaround for excessive logging from JBrowserDriver
		Logger l = Logger.getLogger("com.machinepublishers.jbrowserdriver");
		l.setFilter(record -> !record.getMessage().contains("] DEBUG org.apache."));
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Before
	public void createDriver() {
		Settings settings = Settings.builder().requestHeaders(RequestHeaders.CHROME)
				.userAgent(new UserAgent(UserAgent.Family.WEBKIT, "Google Inc.", "Win32", "Windows NT 6.1",
						"5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2869.0 Safari/537.36",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2869.0 Safari/537.36"))
				.logJavascript(true).logTrace(false).build();

		JBrowserDriver driver = new JBrowserDriver(settings);
		// JBrowserDriver driver = new JBrowserDriver();

		// ChromeDriver driver = new ChromeDriver();
		setDriver(driver);
		CurrentDriver.set(driver);
	}

}
