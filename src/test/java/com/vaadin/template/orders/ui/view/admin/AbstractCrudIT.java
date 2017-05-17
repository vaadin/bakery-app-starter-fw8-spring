package com.vaadin.template.orders.ui.view.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.template.orders.AbstractOrdersIT;
import com.vaadin.template.orders.ui.view.admin.product.CrudViewElement;
import com.vaadin.testbench.elements.GridElement;

public abstract class AbstractCrudIT extends AbstractOrdersIT {

	@Test
	public void sortGrid() {
		CrudViewElement userAdmin = loginAndNavigateToView();
		GridElement grid = userAdmin.getList();
		List<String[]> currentData = getData(grid);

		int columnCount = getColumnCount(grid);
		for (int i = 0; i < columnCount; i++) {
			sort(currentData, i, false);
			grid.getHeaderCell(0, i).click();
			assertData(currentData, getData(grid));

			sort(currentData, i, true);
			grid.getHeaderCell(0, i).click();
			assertData(currentData, getData(grid));
		}
	}

	@Test
	public void filterGrid() {
		CrudViewElement view = loginAndNavigateToView();
		GridElement grid = view.getList();
		List<String[]> currentData = getData(grid);

		view.getSearch().setValue("bak");
		List<String[]> shouldMatch = filter(currentData, "bak");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		view.getSearch().setValue("ba");
		shouldMatch = filter(currentData, "ba");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		view.getSearch().setValue("a");
		shouldMatch = filter(currentData, "a");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());
	}

	protected abstract CrudViewElement loginAndNavigateToView();

	protected static void assertData(List<String[]> expected, List<String[]> data) {
		Assert.assertEquals(expected.size(), data.size());
		for (int i = 0; i < expected.size(); i++) {
			String[] expectedRow = expected.get(i);
			String[] actualRow = data.get(i);

			Assert.assertArrayEquals(expectedRow, actualRow);
		}
	}

	protected static void sort(List<String[]> currentData, int i, boolean reverse) {
		Collections.sort(currentData, (o1, o2) -> {
			if (!reverse) {
				return o1[i].compareTo(o2[i]);
			} else {
				return o2[i].compareTo(o1[i]);
			}
		});

	}

	protected static List<String[]> filter(List<String[]> haystack, String needle) {
		List<String[]> matches = new ArrayList<>();
		for (String[] data : haystack) {
			if (anyContains(data, needle)) {
				matches.add(data);
			}
		}
		return matches;
	}

	protected static boolean anyContains(String[] data, String needle) {
		for (int i = 0; i < data.length; i++) {
			if (data[i].toLowerCase().contains(needle.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
