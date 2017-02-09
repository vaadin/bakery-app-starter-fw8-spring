package com.vaadin.template.orders.ui.view.admin;

public class UserAdminPresenter {

    private UserAdminView view;

    void init(UserAdminView view) {
        this.view = view;
    }

    protected UserAdminView getView() {
        return view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }
}
