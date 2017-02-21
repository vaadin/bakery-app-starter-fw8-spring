package com.vaadin.template.orders.ui.view.orders;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;

@SpringView
public class OrdersListView extends OrdersListViewDesign implements View {

    @Autowired
    private OrdersListPresenter presenter;

    /**
     * This method is invoked once each time an instance of the view is created.
     * <p>
     * This typically happens whenever a user opens the URL for the view, or
     * refreshes the browser as long as the view is set to {@link ViewScope}. If
     * we set the view to {@link UIScope}, the instance will be kept in memory
     * (in the session) as long as the UI exists in memory and the same view
     * instance will be reused whenever the user enters the view.
     * <p>
     * Here we set up listeners and attach data providers and otherwise
     * configure the components for the parts which only need to be done once.
     */
    @PostConstruct
    public void init() {
        presenter.init(this);
        list.setDataProvider(presenter.getOrdersProvider());
        list.addSelectionListener(
                e -> presenter.selectedOrder(e.getFirstSelectedItem().get()));
        newOrder.addClickListener(e -> presenter.newOrder());

    }

    /**
     * This is called whenever the user enters the view, regardless of if the
     * view instance was created right before this or if an old instance was
     * reused.
     * <p>
     * Here we update the data shown in the view so the user sees the latest
     * changes.
     */
    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
