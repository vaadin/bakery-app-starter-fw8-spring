package com.vaadin.template.orders.ui.view.admin.user;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.dashboard.DashboardViewElement;
import com.vaadin.template.orders.ui.view.object.LoginViewElement;
import com.vaadin.template.orders.ui.view.object.MenuElement;
import com.vaadin.template.orders.ui.view.orders.ElementUtil;
import com.vaadin.testbench.By;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.GridElement.GridCellElement;
import com.vaadin.testbench.elements.GridElement.GridRowElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class UserAdminIT extends AbstractOrdersIT {

	@Test
	public void updatePassword() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Users"));
		UserAdminViewElement userAdmin = UserAdminViewElement.get();

		// Change the password for baker to foo and back to baker
		TestBenchElement bakerCell = getCell(userAdmin.getList(), "baker@vaadin.com");
		bakerCell.click();

		TextFieldElement passwordField = userAdmin.getPassword();
		Assert.assertEquals("", passwordField.getValue());

		passwordField.setValue("foo");
		ButtonElement update = userAdmin.getUpdate();
		update.click();

		assertEnabled(false, userAdmin.getForm());

		bakerCell.click();
		Assert.assertEquals("", passwordField.getValue());
		passwordField.setValue("baker");
		update.click();

		assertEnabled(false, userAdmin.getForm());
	}

	/**
	 * Finds the cell with the given content and returns it.
	 *
	 * @param grid
	 *            the grid to search through
	 * @param contents
	 *            the contents to look for
	 * @return the first cell with a matching content
	 * @throws NoSuchElementException
	 *             if no cell was found
	 */
	private static TestBenchElement getCell(GridElement grid, String contents) throws NoSuchElementException {
		int columns = getColumnCount(grid);
		for (GridRowElement row : grid.getRows()) {
			for (int i = 0; i < columns; i++) {
				GridCellElement cell = row.getCell(i);
				if (contents.equals(cell.getText())) {
					return cell;
				}
			}
		}

		throw new NoSuchElementException("No cell with text '" + contents + "' found");
	}

	/**
	 * Gets the number of columns shown in the grid.
	 * <p>
	 * Assumes that the grid contains at least one row.
	 *
	 * @param grid
	 *            the grid to query
	 * @return the number of columns in the grid
	 */
	private static int getColumnCount(GridElement grid) {
		return grid.getRow(0).findElements(By.xpath("./td")).size();
	}
}
