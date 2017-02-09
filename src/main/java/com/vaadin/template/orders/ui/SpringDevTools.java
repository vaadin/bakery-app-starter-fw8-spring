package com.vaadin.template.orders.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.declarative.Design.DefaultComponentFactory;
import com.vaadin.ui.declarative.DesignContext;
import com.vaadin.ui.declarative.DesignException;

public class SpringDevTools {

    public static void fix() {
        // Workaround for spring-devtools issue
        // https://github.com/vaadin/framework/issues/8528
        Design.setComponentFactory(new DefaultComponentFactory() {
            @Override
            protected Class<? extends Component> resolveComponentClass(
                    String qualifiedClassName, DesignContext context) {
                try {
                    return (Class<? extends Component>) Class
                            .forName(qualifiedClassName);
                } catch (ClassNotFoundException e) {
                    throw new DesignException("Unable to load class", e);
                }
            }
        });

    }

}
