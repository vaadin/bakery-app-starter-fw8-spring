package com.vaadin.template.orders.ui.view.dashboard;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.components.OrdersGrid;
import com.vaadin.template.orders.ui.view.OrdersView;

@SpringView
public class DashboardView extends DashboardViewDesign implements OrdersView {

	@Autowired
	private DashboardPresenter presenter;

	@Autowired
	private Logger logger;

	private final BoardLabel todayLabel = new BoardLabel("Today", "3/7");
	private final BoardLabel notAvailableLabel = new BoardLabel("N/A", "1");
	private final BoardLabel newLabel = new BoardLabel("New", "2");
	private final BoardLabel tomorrowLabel = new BoardLabel("Tomorrow", "4");
	private final Chart deliveriesThisMonthGraph = new Chart(ChartType.LINE);
	private final Chart deliveriesThisYearGraph = new Chart(ChartType.LINE);
	private final Chart yearlySalesGraph = new Chart(ChartType.LINE);
	private final Chart monthlyProductSplit = new Chart(ChartType.PIE);
	private final OrdersGrid dueGrid = new OrdersGrid();

	private ListSeries deliveriesThisMonthSeries;
	private ListSeries deliveriesThisYearSeries;
	private ListSeries[] salesPerYear;

	private DataSeries deliveriesPerProductSeries;

	@PostConstruct
	public void init() {
		board.addRow(new BoardBox(todayLabel), new BoardBox(notAvailableLabel), new BoardBox(newLabel),
				new BoardBox(tomorrowLabel));
		board.addRow(deliveriesThisMonthGraph, deliveriesThisYearGraph);
		board.addRow(yearlySalesGraph);
		board.addRow(monthlyProductSplit, dueGrid);

		initDeliveriesGraphs();
		initProductSplitMonthlyGraph();
		initYearlySalesGraph();

		dueGrid.setId("dueGrid");
		dueGrid.setHeight("300px");
		dueGrid.addStyleName("border");

		dueGrid.setDataProvider(presenter.getOrdersProvider());
	}

	private void initYearlySalesGraph() {
		yearlySalesGraph.setId("yearlySales");
		yearlySalesGraph.setHeight("400px");
		int year = Year.now().getValue();

		Configuration conf = yearlySalesGraph.getConfiguration();
		conf.setTitle("Sales last years");
		conf.getxAxis().setCategories(getMonthNames());

		salesPerYear = new ListSeries[3];
		for (int i = 0; i < 3; i++) {
			salesPerYear[i] = new ListSeries(Integer.toString(year - i));
			conf.addSeries(salesPerYear[i]);
		}
	}

	private void initProductSplitMonthlyGraph() {
		monthlyProductSplit.setId("monthlyProductSplit");
		monthlyProductSplit.setHeight("300px");
		monthlyProductSplit.setWidth("300px");

		LocalDate today = LocalDate.now();

		Configuration conf = monthlyProductSplit.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		conf.setTitle("Products delivered in " + thisMonth);
		deliveriesPerProductSeries = new DataSeries();
		conf.addSeries(deliveriesPerProductSeries);
	}

	private void initDeliveriesGraphs() {
		LocalDate today = LocalDate.now();

		deliveriesThisMonthGraph.setId("deliveriesThisMonth");
		deliveriesThisMonthGraph.setHeight("200px");
		deliveriesThisMonthGraph.addStyleName("v-clip");
		deliveriesThisYearGraph.setId("deliveriesThisYear");
		deliveriesThisYearGraph.setHeight("200px");
		deliveriesThisYearGraph.addStyleName("v-clip");

		Configuration yearConf = deliveriesThisYearGraph.getConfiguration();

		yearConf.setTitle("Deliveries in " + today.getYear());
		yearConf.getxAxis().setCategories(getMonthNames());
		yearConf.getLegend().setEnabled(false);
		deliveriesThisYearSeries = new ListSeries("Deliveries");
		yearConf.addSeries(deliveriesThisYearSeries);

		Configuration monthConf = deliveriesThisMonthGraph.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		monthConf.setTitle("Deliveries in " + thisMonth);
		monthConf.getLegend().setEnabled(false);
		deliveriesThisMonthSeries = new ListSeries("Deliveries");
		monthConf.addSeries(deliveriesThisMonthSeries);
		int daysInMonth = YearMonth.of(today.getYear(), today.getMonthValue()).lengthOfMonth();
		String[] categories = IntStream.rangeClosed(1, daysInMonth).mapToObj(Integer::toString)
				.toArray(size -> new String[size]);
		deliveriesThisMonthSeries.getConfiguration().getxAxis().setCategories(categories);

	}

	private String[] getMonthNames() {
		return Stream.of(Month.values()).map(month -> month.getDisplayName(TextStyle.SHORT, Locale.US))
				.toArray(size -> new String[size]);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardData data = presenter.fetchData();
		logger.info("Update graphs started");
		updateLabels(data.getDeliveryStats());
		updateGraphs(data);
		logger.info("Update graphs done");
	}

	private void updateGraphs(DashboardData data) {
		deliveriesThisMonthSeries.setData(data.getDeliveriesThisMonth());
		deliveriesThisYearSeries.setData(data.getDeliveriesThisYear());

		for (int i = 0; i < 3; i++) {
			salesPerYear[i].setData(data.getSalesPerMonth(i));
		}

		for (Entry<Product, Integer> entry : data.getProductDeliveries().entrySet()) {
			deliveriesPerProductSeries.add(new DataSeriesItem(entry.getKey().getName(), entry.getValue()));
		}
	}

	private void updateLabels(DeliveryStats deliveryStats) {
		todayLabel.setContent(deliveryStats.getDeliveredToday() + "/" + deliveryStats.getDueToday());
		notAvailableLabel.setContent(Integer.toString(deliveryStats.getNotAvailableToday()));
		if (deliveryStats.getNotAvailableToday() > 0) {
			notAvailableLabel.setNeedsAttention(true);
		}
		newLabel.setContent(Integer.toString(deliveryStats.getNewOrders()));
		tomorrowLabel.setContent(Integer.toString(deliveryStats.getDueTomorrow()));

		notAvailableLabel.setStyleName("problem", deliveryStats.getNotAvailableToday() > 0);
	}
}
