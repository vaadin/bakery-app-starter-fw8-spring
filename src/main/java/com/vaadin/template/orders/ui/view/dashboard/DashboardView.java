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

import com.vaadin.template.orders.ui.components.OrdersDataProvider;
import com.vaadin.template.orders.ui.navigation.NavigationManager;
import com.vaadin.template.orders.ui.view.orders.OrdersListView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.board.Row;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.DashboardData;
import com.vaadin.template.orders.backend.data.DeliveryStats;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.HasLogger;
import com.vaadin.template.orders.ui.components.OrdersGrid;
import com.vaadin.template.orders.ui.view.NavigableView;

@SpringView
public class DashboardView extends DashboardViewDesign implements NavigableView, HasLogger {

	private static final String BOARD_ROW_PANELS = "board-row-panels";

	private final DashboardPresenter presenter;

	private final NavigationManager navigationManager;

	private final BoardLabel todayLabel = new BoardLabel("Today", "3/7", "today");
	private final BoardLabel notAvailableLabel = new BoardLabel("N/A", "1", "na");
	private final BoardLabel newLabel = new BoardLabel("New", "2", "new");
	private final BoardLabel tomorrowLabel = new BoardLabel("Tomorrow", "4", "tomorrow");
	private final BoardBox todayBox = new BoardBox(todayLabel);
	private final BoardBox notAvailableBox = new BoardBox(notAvailableLabel);
	private final BoardBox newBox = new BoardBox(newLabel);
	private final BoardBox tomorrowBox = new BoardBox(tomorrowLabel);


	private final Chart deliveriesThisMonthGraph = new Chart(ChartType.COLUMN);
	private final Chart deliveriesThisYearGraph = new Chart(ChartType.COLUMN);
	private final Chart yearlySalesGraph = new Chart(ChartType.AREASPLINE);
	private final Chart monthlyProductSplit = new Chart(ChartType.PIE);
	private final OrdersGrid dueGrid = new OrdersGrid();

	private ListSeries deliveriesThisMonthSeries;
	private ListSeries deliveriesThisYearSeries;
	private ListSeries[] salesPerYear;

	private DataSeries deliveriesPerProductSeries;

	@Autowired
	public DashboardView(DashboardPresenter presenter, NavigationManager navigationManager) {
		this.presenter = presenter;
		this.navigationManager = navigationManager;
	}

	@PostConstruct
	public void init() {
		setResponsive(true);

		todayBox.addLayoutClickListener(e -> navigationManager.navigateTo(OrdersListView.class, ""));

		Row row = board.addRow(todayBox, notAvailableBox, newBox, tomorrowBox);
		row.addStyleName("board-row-group");

		row = board.addRow(new BoardBox(deliveriesThisMonthGraph), new BoardBox(deliveriesThisYearGraph));
		row.addStyleName(BOARD_ROW_PANELS);

		row = board.addRow(new BoardBox(yearlySalesGraph));
		row.addStyleName(BOARD_ROW_PANELS);

		row = board.addRow(new BoardBox(monthlyProductSplit), new BoardBox(dueGrid, "due-grid"));
		row.addStyleName(BOARD_ROW_PANELS);

		initDeliveriesGraphs();
		initProductSplitMonthlyGraph();
		initYearlySalesGraph();

		dueGrid.setId("dueGrid");
		dueGrid.setSizeFull();

		dueGrid.setDataProvider(presenter.getOrdersProvider());
	}

	private void initYearlySalesGraph() {
		yearlySalesGraph.setId("yearlySales");
		yearlySalesGraph.setSizeFull();
		int year = Year.now().getValue();

		Configuration conf = yearlySalesGraph.getConfiguration();
		conf.setTitle("Sales last years");
		configureTitleStyle(conf);
		conf.getxAxis().setCategories(getMonthNames());
		conf.getChart().setMarginBottom(6);

		salesPerYear = new ListSeries[3];
		for (int i = 0; i < 3; i++) {
			salesPerYear[i] = new ListSeries(Integer.toString(year - i));
			conf.addSeries(salesPerYear[i]);
		}
		conf.getyAxis().setTitle("");

	}

	private void initProductSplitMonthlyGraph() {
		monthlyProductSplit.setId("monthlyProductSplit");
		monthlyProductSplit.setSizeFull();

		LocalDate today = LocalDate.now();

		Configuration conf = monthlyProductSplit.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		conf.setTitle("Products delivered in " + thisMonth);
		configureTitleStyle(conf);
		deliveriesPerProductSeries = new DataSeries();
		conf.addSeries(deliveriesPerProductSeries);

		conf.getyAxis().setTitle("");

	}

	private void initDeliveriesGraphs() {
		LocalDate today = LocalDate.now();

		deliveriesThisMonthGraph.setId("deliveriesThisMonth");
		deliveriesThisMonthGraph.setSizeFull();

		deliveriesThisYearGraph.setId("deliveriesThisYear");
		deliveriesThisYearGraph.setSizeFull();

		Configuration yearConf = deliveriesThisYearGraph.getConfiguration();

		yearConf.setTitle("Deliveries in " + today.getYear());
		configureTitleStyle(yearConf);
		yearConf.getChart().setMarginBottom(6);
		yearConf.getxAxis().setCategories(getMonthNames());
		yearConf.getxAxis().setLabels(new Labels(null));
		yearConf.getLegend().setEnabled(false);
		deliveriesThisYearSeries = new ListSeries("Deliveries");
		yearConf.addSeries(deliveriesThisYearSeries);
		configureColumnSeries(deliveriesThisYearSeries);

		Configuration monthConf = deliveriesThisMonthGraph.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		monthConf.setTitle("Deliveries in " + thisMonth);
		configureTitleStyle(monthConf);
		monthConf.getChart().setMarginBottom(6);
		monthConf.getLegend().setEnabled(false);
		deliveriesThisMonthSeries = new ListSeries("Deliveries");
		monthConf.addSeries(deliveriesThisMonthSeries);
		configureColumnSeries(deliveriesThisMonthSeries);

		int daysInMonth = YearMonth.of(today.getYear(), today.getMonthValue()).lengthOfMonth();
		String[] categories = IntStream.rangeClosed(1, daysInMonth).mapToObj(Integer::toString)
				.toArray(size -> new String[size]);
		monthConf.getxAxis().setCategories(categories);
		monthConf.getxAxis().setLabels(new Labels(false));

	}

	protected void configureColumnSeries(ListSeries series) {
		PlotOptionsColumn options = new PlotOptionsColumn();
		options.setBorderWidth(1);
		options.setGroupPadding(0);
		series.setPlotOptions(options);

		YAxis yaxis = series.getConfiguration().getyAxis();
		yaxis.setGridLineWidth(0);
		yaxis.setLabels(new Labels(false));
		yaxis.setTitle("");
	}

	private void configureTitleStyle(Configuration conf) {
		Style titleStyle = conf.getTitle().getStyle();
		titleStyle.setColor(new SolidColor("inherit"));
		titleStyle.setFontSize("inherit");
	}

	private String[] getMonthNames() {
		return Stream.of(Month.values()).map(month -> month.getDisplayName(TextStyle.SHORT, Locale.US))
				.toArray(size -> new String[size]);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardData data = presenter.fetchData();
		getLogger().info("Update graphs started");
		updateLabels(data.getDeliveryStats());
		updateGraphs(data);
		getLogger().info("Update graphs done");
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
		notAvailableBox.setNeedsAttention(deliveryStats.getNotAvailableToday() > 0);
		newLabel.setContent(Integer.toString(deliveryStats.getNewOrders()));
		tomorrowLabel.setContent(Integer.toString(deliveryStats.getDueTomorrow()));
	}

}
