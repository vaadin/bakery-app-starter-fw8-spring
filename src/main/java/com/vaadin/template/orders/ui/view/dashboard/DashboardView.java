package com.vaadin.template.orders.ui.view.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.ui.components.BulletinBoard;
import com.vaadin.template.orders.ui.components.OrdersGrid;
import com.vaadin.ui.Label;

@SpringView(name = "")
public class DashboardView extends DashboardViewDesign implements View {

    private static final String CENTER_BORDER = "center border";

    @Autowired
    private DashboardPresenter presenter;

    private final BoardLabel todayLabel = new BoardLabel("Today", "3/7");
    private final BoardLabel notAvailableLabel = new BoardLabel("N/A", "1");
    private final BoardLabel newLabel = new BoardLabel("New", "2");
    private final BoardLabel tomorrowLabel = new BoardLabel("Tomorrow", "4");
    private final Label deliveriesThisMonthGraph = new Label(
            "month graph placeholder");
    private final Label deliveriesThisYearGraph = new Label(
            "year graph placeholder");
    private final Label yearlySalesGraph = new Label("sales graph placeholder");
    private final Label monthlyProductSplit = new Label(
            "monthlyProductSplit placeholder");
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

        deliveriesThisMonthGraph.setHeight("75px");
        deliveriesThisMonthGraph.addStyleName(CENTER_BORDER);
        deliveriesThisYearGraph.setHeight("75px");
        deliveriesThisYearGraph.addStyleName(CENTER_BORDER);

        yearlySalesGraph.setHeight("200px");
        yearlySalesGraph.addStyleName(CENTER_BORDER);

        deliveriesThisYearGraph.addStyleName(CENTER_BORDER);

        dueGrid.setHeight("300px");
        dueGrid.addStyleName("border");
        monthlyProductSplit.setHeight("300px");
        monthlyProductSplit.setWidth("300px");
        monthlyProductSplit.addStyleName(CENTER_BORDER);

        dueGrid.setDataProvider(presenter.getOrdersProvider());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }
}
