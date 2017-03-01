package com.vaadin.template.orders.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class AccessDeniedView extends AccessDeniedDesign implements View {

    @Override
    public void enter(ViewChangeEvent event) {
        // Nothing to do, just show the view
    }

}
