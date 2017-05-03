package com.vaadin.template.orders.ui.view.dashboard;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.components.OrdersGrid;
import com.vaadin.template.orders.ui.view.OrdersView;

@SpringView
public class DashboardView extends DashboardViewDesign implements OrdersView {

    @Autowired
    private DashboardPresenter presenter;

    private final BoardLabel todayLabel = new BoardLabel("Today", "3/7");
    private final BoardLabel notAvailableLabel = new BoardLabel("N/A", "1");
    private final BoardLabel newLabel = new BoardLabel("New", "2");
    private final BoardLabel tomorrowLabel = new BoardLabel("Tomorrow", "4");
    private final Chart deliveriesThisMonthGraph = new Chart(ChartType.LINE);
    private final Chart deliveriesThisYearGraph = new Chart(ChartType.LINE);
    private final Chart yearlySalesGraph = new Chart(ChartType.LINE);
    private final Chart monthlyProductSplit = new Chart(ChartType.PIE);
    private final OrdersGrid dueGrid = new OrdersGrid();

    @PostConstruct
    public void init() {
        getLogger().info("DashBoardView.init()");
        board.addRow(new BoardBox(todayLabel), new BoardBox(notAvailableLabel),
                new BoardBox(newLabel), new BoardBox(tomorrowLabel));
        board.addRow(deliveriesThisMonthGraph, deliveriesThisYearGraph);
        board.addRow(yearlySalesGraph);
        board.addRow(monthlyProductSplit, dueGrid);
        getLogger().info("... board set up");

        initDeliveriesGraphs();
        getLogger().info("... devlieries graphs set up");
        initProductSplitMonthlyGraph();
        getLogger().info("... product split graphs set up");
        initYearlySalesGraph();
        getLogger().info("... yearly sales graphs set up");

        dueGrid.setHeight("300px");
        dueGrid.addStyleName("border");

        dueGrid.setDataProvider(presenter.getOrdersProvider());
        getLogger().info("DashBoardView.init() done");
    }

    private void initYearlySalesGraph() {
        yearlySalesGraph.setHeight("400px");
        int now = Year.now().getValue();
        List<Double> thisYear = presenter.getSalesPerMonth(now);
        List<Double> oneYearBack = presenter.getSalesPerMonth(now - 1);
        List<Double> twoYearsBack = presenter.getSalesPerMonth(now - 2);

        Configuration conf = yearlySalesGraph.getConfiguration();
        conf.setTitle("Sales last years");
        conf.getxAxis().setCategories(getMonthNames());
        conf.addSeries(new MyListSeries(Integer.toString(now), thisYear));
        conf.addSeries(
                new MyListSeries(Integer.toString(now - 1), oneYearBack));
        conf.addSeries(
                new MyListSeries(Integer.toString(now - 2), twoYearsBack));

    }

    private void initProductSplitMonthlyGraph() {
        monthlyProductSplit.setHeight("300px");
        monthlyProductSplit.setWidth("300px");

        LocalDate today = LocalDate.now();

        Configuration conf = monthlyProductSplit.getConfiguration();
        String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL,
                Locale.US);
        conf.setTitle("Products delivered in " + thisMonth);
        LinkedHashMap<Product, Integer> deliveries = presenter
                .getDeliveriesPerProduct(today.getMonthValue(),
                        today.getYear());

        final DataSeries series = new DataSeries();
        for (Entry<Product, Integer> entry : deliveries.entrySet()) {
            series.add(new DataSeriesItem(entry.getKey().getName(),
                    entry.getValue()));
        }

        conf.addSeries(series);
    }

    private void initDeliveriesGraphs() {
        deliveriesThisMonthGraph.setHeight("200px");
        deliveriesThisMonthGraph.addStyleName("v-clip");
        deliveriesThisYearGraph.setHeight("200px");
        deliveriesThisYearGraph.addStyleName("v-clip");

        Configuration monthConf = deliveriesThisMonthGraph.getConfiguration();
        Configuration yearConf = deliveriesThisYearGraph.getConfiguration();

        LocalDate today = LocalDate.now();

        yearConf.setTitle("Deliveries in " + today.getYear());
        yearConf.getxAxis().setCategories(getMonthNames());
        yearConf.addSeries(new MyListSeries("deliveries",
                presenter.getDeliveriesPerMonth(today.getYear())));
        yearConf.getLegend().setEnabled(false);
        String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL,
                Locale.US);
        monthConf.setTitle("Deliveries in " + thisMonth);
        monthConf.getLegend().setEnabled(false);

        monthConf.addSeries(new MyListSeries("deliveries", presenter
                .getDeliveriesPerDay(today.getMonthValue(), today.getYear())));
        int daysInMonth = YearMonth.of(today.getYear(), today.getMonthValue())
                .lengthOfMonth();

        String[] categories = IntStream.rangeClosed(1, daysInMonth)
                .mapToObj(Integer::toString).toArray(size -> new String[size]);
        monthConf.getxAxis().setCategories(categories);

    }

    private String[] getMonthNames() {
        return Stream.of(Month.values())
                .map(month -> month.getDisplayName(TextStyle.SHORT, Locale.US))
                .toArray(size -> new String[size]);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        OrderStats stats = presenter.fetchStats();

        todayLabel.setContent(
                stats.getDeliveredToday() + "/" + stats.getDueToday());
        notAvailableLabel
                .setContent(Integer.toString(stats.getNotAvailableToday()));
        if (stats.getNotAvailableToday() > -1) {
            notAvailableLabel.setNeedsAttention(true);
        }
        newLabel.setContent(Integer.toString(stats.getUnverified()));
        tomorrowLabel.setContent(Integer.toString(stats.getDueTomorrow()));

        notAvailableLabel.setStyleName("problem",
                stats.getNotAvailableToday() > 0);
    }

    private static Logger getLogger() {
        return Logger.getLogger(DashboardView.class.getName());
    }

}
