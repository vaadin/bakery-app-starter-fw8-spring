package com.vaadin.template.orders.ui.view.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.framework.FrameworkDataHelper;

@SpringComponent
public class OrdersPresenter {

    private final OrderRepository orderRepository;
    private OrdersView view;

    @Autowired
    public OrdersPresenter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    void init(OrdersView view) {
        this.view = view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }

    protected OrdersView getView() {
        return view;
    }

    /**
     * Gets a data provider which serves available orders according to the fiven
     * .
     *
     * @return a data provider
     */
    public DataProvider<Order, SerializablePredicate<Order>> getOrdersProvider() {
        return new CallbackDataProvider<>(dataQuery -> {
            int firstRequested = dataQuery.getOffset();
            int nrRequested = dataQuery.getLimit();
            Pageable pageable = FrameworkDataHelper.getPageable(dataQuery);

            List<Order> items = orderRepository.findAll(pageable).getContent();
            int firstReturned = pageable.getPageNumber();
            int firstReal = firstRequested - firstReturned;
            int afterLastReal = firstReal + nrRequested;
            if (afterLastReal > items.size()) {
                afterLastReal = items.size();
            }
            return items.subList(firstReal, afterLastReal).stream();
        }, sizeQuery -> (int) orderRepository.count());
    }

}
