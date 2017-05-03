package com.vaadin.template.orders.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

/**
 * An improved view class which can provide the component for the view.
 */
public interface OrdersView extends View {

    /**
     * Gets the component to show when navigating to the view.
     *
     * By default casts this View to a {@link Component}.
     *
     * @return the component to show, by default the view instance itself
     */
    public default Component getViewComponent() {
        return (Component) this;
    }

}
