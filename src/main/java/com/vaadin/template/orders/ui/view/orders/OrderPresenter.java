package com.vaadin.template.orders.ui.view.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class OrderPresenter {

    private final OrderRepository orderRepository;
    private OrderView view;
    private final PickupLocationRepository pickupLocationRepository;

    @Autowired
    public OrderPresenter(OrderRepository orderRepository,
            PickupLocationRepository pickupLocationRepository) {
        this.orderRepository = orderRepository;
        this.pickupLocationRepository = pickupLocationRepository;
    }

    void init(OrderView view) {
        this.view = view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enterView(Long id) {
        Order order;
        if (id == null) {
            // New
            order = new Order();
        } else {
            order = orderRepository.findOne(id);
            if (order == null) {
                // TODO
                return;
            }
        }

        view.editOrder(order);
    }

    protected OrderView getView() {
        return view;
    }

    public List<PickupLocation> getPickupLocations() {
        return pickupLocationRepository.findAll();
    }

}
