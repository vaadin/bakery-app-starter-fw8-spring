package com.vaadin.template.orders.ui.view.dashboard;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class DashboardPresenter {

    @Autowired
    private OrdersDataProvider ordersDataProvider;

    @Autowired
    private OrderRepository repository;

    private DashboardView view;

    void init(DashboardView view) {
        this.view = view;
    }

    protected DashboardView getView() {
        return view;
    }

    public OrdersDataProvider getOrdersProvider() {
        return ordersDataProvider;
    }

    public OrderStats fetchStats() {
        OrderStats stats = new OrderStats();
        LocalDate today = LocalDate.now();
        stats.setDueToday((int) repository.countByDueDate(today));
        stats.setDueTomorrow(
                (int) repository.countByDueDate(today.plusDays(1)));
        stats.setDeliveredToday((int) repository.countByDueDateAndStateIn(today,
                Collections.singleton(OrderState.DELIVERED)));

        Set<OrderState> notAvailableStates = new HashSet<>(
                Arrays.asList(OrderState.values()));
        notAvailableStates.remove(OrderState.DELIVERED);
        notAvailableStates.remove(OrderState.READY_FOR_PICKUP);
        notAvailableStates.remove(OrderState.CANCELLED);

        stats.setNotAvailableToday((int) repository
                .countByDueDateAndStateIn(today, notAvailableStates));
        stats.setUnverified((int) repository.countByState(OrderState.NEW));
        return stats;
    }

    public List<Integer> getDeliveriesPerDay(int month, int year) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        return (List) dataWithMissingAsNull(daysInMonth,
                repository.countPerDay(OrderState.DELIVERED, year, month));
    }

    public List<Integer> getDeliveriesPerMonth(int year) {
        return (List) dataWithMissingAsNull(12,
                repository.countPerMonth(OrderState.DELIVERED, year));
    }

    public List<Double> getSalesPerMonth(int year) {
        return (List) dataWithMissingAsNull(12,
                repository.sumPerMonth(OrderState.DELIVERED, year));
    }

    public LinkedHashMap<Product, Integer> getDeliveriesPerProduct(int month,
            int year) {
        List<Object[]> productDeliveries = repository
                .countPerProduct(OrderState.DELIVERED, year, month);
        LinkedHashMap<Product, Integer> deliveries = new LinkedHashMap<>();
        for (Object[] productDelivery : productDeliveries) {
            int sum = ((Long) productDelivery[0]).intValue();
            Product p = (Product) productDelivery[1];
            deliveries.put(p, sum);
        }
        return deliveries;
    }

    private List<Number> dataWithMissingAsNull(int dataPoints,
            List<Object[]> list) {
        List<Number> counts = new ArrayList<>();
        for (int i = 0; i < dataPoints; i++) {
            counts.add(null);
        }

        for (Object[] result : list) {
            counts.set((Integer) result[0] - 1, ((Number) result[1]));
        }
        return counts;
    }

}
