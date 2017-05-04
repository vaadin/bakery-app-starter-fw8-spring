package com.vaadin.template.orders.ui.view.dashboard;

import java.util.List;

import com.vaadin.addon.charts.model.ListSeries;

public class MyListSeries extends ListSeries {

	public MyListSeries(String name, List<? extends Number> deliveriesForMonth) {
		super(name, deliveriesForMonth.toArray(new Number[deliveriesForMonth.size()]));
	}

}
