package com.vaadin.template.orders.ui.view.dashboard;

import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class DashboardPresenter {

    private DashboardView view;

    void init(DashboardView view) {
        this.view = view;
    }

    protected DashboardView getView() {
        return view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }
}
