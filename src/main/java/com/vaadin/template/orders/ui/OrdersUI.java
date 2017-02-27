package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.template.orders.ui.view.orders.OrderEditView;
import com.vaadin.ui.UI;

@Theme("orderstheme")
@SpringUI
@Viewport("width=device-width, initial-scale=1.0")
public class OrdersUI extends UI {

    @Autowired
    private MainView mainView;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(mainView);
    }

    /**
     * Find the view id (URI fragment) used for a given view class.
     *
     * @param viewClass
     *            the view class to find the id for
     * @return the URI fragment for the view
     */
    public static String getViewId(Class<? extends View> viewClass) {
        SpringView springView = viewClass.getAnnotation(SpringView.class);
        if (springView == null) {
            throw new IllegalArgumentException(
                    "The target class must be a @SpringView");
        }

        return Conventions.deriveMappingForView(viewClass, springView);
    }

    public void navigateTo(Class<? extends View> targetView) {
        String viewId = getViewId(targetView);
        getNavigator().navigateTo(viewId);
    }

    public void navigateTo(Class<OrderEditView> targetView, Object parameter) {
        String viewId = getViewId(targetView);
        getNavigator().navigateTo(viewId + "/" + parameter.toString());
    }

}
