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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.vaadin.addon.charts.model.*;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.template.orders.backend.dto.SalesPerMonthDTO;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.components.BulletinBoard;
import com.vaadin.template.orders.ui.components.OrdersGrid;

@SpringView(name = "")
public class DashboardView extends DashboardViewDesign implements View {

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
        board.addRow(todayLabel, notAvailableLabel, newLabel, tomorrowLabel);
        board.addRow(deliveriesThisMonthGraph, deliveriesThisYearGraph);
        board.addRow(yearlySalesGraph);
        board.addRow(monthlyProductSplit, dueGrid);
        board.addRow(new BulletinBoard());

        // Dummy configuration for now
        todayLabel.setHeight("150px");
        tomorrowLabel.setHeight("150px");
        notAvailableLabel.setHeight("150px");
        newLabel.setHeight("150px");

        initDeliveriesGraphs();
        initProductSplitMonthlyGraph();
        initYearlySalesGraph();

        dueGrid.setHeight("300px");
        dueGrid.addStyleName("border");

        dueGrid.setDataProvider(presenter.getOrdersProvider());
    }

    private void initYearlySalesGraph() {
        yearlySalesGraph.setHeight("400px");
        int now = Year.now().getValue();
        List<SalesPerMonthDTO> threeYears = presenter.getSalesPerMonthLastThreeYears();
        List<SalesPerMonthDTO> twoYearsBack = threeYears.stream().filter(dto->dto.getYear()==now-2).collect(Collectors.toList());
        List<SalesPerMonthDTO> oneYearBack = threeYears.stream().filter(dto->dto.getYear()==now-1).collect(Collectors.toList());
        List<SalesPerMonthDTO> thisYear = threeYears.stream().filter(dto->dto.getYear()==now).collect(Collectors.toList());

        Configuration conf = yearlySalesGraph.getConfiguration();
        conf.setTitle("Sales last years");
        conf.getxAxis().setCategories(getMonthNames());

        DataProvider<SalesPerMonthDTO, ?> thisYearDataProvider = new ListDataProvider<>(thisYear);
        DataProvider<SalesPerMonthDTO, ?> oneYearBackDataProvider = new ListDataProvider<>(oneYearBack);
        DataProvider<SalesPerMonthDTO, ?> twoYearsBackDataProvider = new ListDataProvider<>(twoYearsBack);
        DataProviderSeries<SalesPerMonthDTO> thisYearSeries = new DataProviderSeries<>(thisYearDataProvider, SalesPerMonthDTO::getSales);
        DataProviderSeries<SalesPerMonthDTO> oneYearSeries = new DataProviderSeries<>(oneYearBackDataProvider, SalesPerMonthDTO::getSales);
        DataProviderSeries<SalesPerMonthDTO> twoYearsSeries = new DataProviderSeries<>(twoYearsBackDataProvider, SalesPerMonthDTO::getSales);
        thisYearSeries.setName(String.valueOf(now));
        oneYearSeries.setName(String.valueOf(now-1));
        twoYearsSeries.setName(String.valueOf(now-2));


        conf.addSeries(thisYearSeries);
        conf.addSeries(oneYearSeries);
        conf.addSeries(twoYearsSeries);
    }

    private void initProductSplitMonthlyGraph() {
        monthlyProductSplit.setHeight("300px");
        monthlyProductSplit.setWidth("300px");

        LocalDate today = LocalDate.now();

        Configuration conf = monthlyProductSplit.getConfiguration();
        String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL,
                Locale.ENGLISH);
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
                Locale.ENGLISH);
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
        return Stream.of(Month.values()).map(
                month -> month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                .toArray(size -> new String[size]);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        OrderStats stats = presenter.fetchStats();

        todayLabel.setContent(
                stats.getDeliveredToday() + "/" + stats.getDueToday());
        notAvailableLabel
                .setContent(Integer.toString(stats.getNotAvailableToday()));
        newLabel.setContent(Integer.toString(stats.getUnverified()));
        tomorrowLabel.setContent(Integer.toString(stats.getDueTomorrow()));

        notAvailableLabel.setStyleName("problem",
                stats.getNotAvailableToday() > 0);
    }
}
