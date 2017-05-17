package com.vaadin.template.orders.ui.view.admin.user;

import java.util.ArrayList;
import java.util.List;

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

	private UserAdminViewElement loginAndNavigateToAdmin() {
		DashboardViewElement dashboard = LoginViewElement.loginAsAdmin();
		MenuElement menu = dashboard.getMainView().getMenu();
		ElementUtil.click(menu.getMenuLink("Users"));
		return UserAdminViewElement.get();

	}

	@Test
	public void filterGrid() {
		UserAdminViewElement userAdmin = loginAndNavigateToAdmin();
		GridElement grid = userAdmin.getList();
		List<String[]> currentData = getData(grid);

		userAdmin.getSearch().setValue("bak");
		List<String[]> shouldMatch = filter(currentData, "bak");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		userAdmin.getSearch().setValue("ba");
		shouldMatch = filter(currentData, "ba");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		userAdmin.getSearch().setValue("a");
		shouldMatch = filter(currentData, "a");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());
	}

	private List<String[]> filter(List<String[]> haystack, String needle) {
		List<String[]> matches = new ArrayList<>();
		for (String[] data : haystack) {
			if (anyContains(data, needle)) {
				matches.add(data);
			}
		}
		return matches;
	}

	private boolean anyContains(String[] data, String needle) {
		for (int i = 0; i < data.length; i++) {
			if (data[i].contains(needle)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets all visible cell contents from the given grid.
	 *
	 * @param grid
	 *            the grid to check
	 * @return text contents of all cells in the grid
	 */
	private List<String[]> getData(GridElement grid) {
		int cols = getColumnCount(grid);
		ArrayList<String[]> ret = new ArrayList<>();
		for (GridRowElement row : grid.getRows()) {
			String[] rowData = new String[cols];
			for (int i = 0; i < cols; i++) {
				rowData[i] = row.getCell(i).getText();
			}
			ret.add(rowData);
		}
		return ret;
	}

	@Test
	public void updatePassword() {
		UserAdminViewElement userAdmin = loginAndNavigateToAdmin();
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
