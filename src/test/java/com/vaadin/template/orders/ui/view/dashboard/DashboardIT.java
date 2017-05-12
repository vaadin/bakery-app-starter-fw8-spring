package com.vaadin.template.orders.ui.view.dashboard;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elementsbase.ServerClass;

public class DashboardIT extends AbstractOrdersIT {

	@ServerClass("com.vaadin.template.orders.ui.view.dashboard.BoardBox")
	public static class BoardBoxElement extends CssLayoutElement {

		public String getHeader() {
			return ElementUtil.getText((TestBenchElement) findElement(By.className("board-box-small-text")));
		}

		public String getContent() {
			return ElementUtil.getText((TestBenchElement) findElement(By.className("board-box-big-text")));
		}
	}

	@Test
	public void dashboardContainsData() {
		DashboardViewElement dashboardView = LoginViewElement.loginAsAdmin();
		List<WebElement> boxes = dashboardView.getBoard().findElements(By.className("board-box-label"));
		Assert.assertEquals(4, boxes.size());

	}
}
