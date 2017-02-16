package com.vaadin.template.orders.ui.view.admin.product;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class ProductAdminPresenter {

    private ProductAdminView view;

    void init(ProductAdminView view) {
        this.view = view;
    }

    protected ProductAdminView getView() {
        return view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }
}
