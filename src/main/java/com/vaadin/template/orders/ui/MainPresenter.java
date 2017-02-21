package com.vaadin.template.orders.ui;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.app.Application;

@SpringComponent
@PrototypeScope
public class MainPresenter {

    private MainView view;

    void init(MainView view) {
        this.view = view;
    }

    /**
     * Navigate to the given view class.
     *
     * @param viewClass
     *            the class of the target view, must be annotated using
     *            {@link SpringView @SpringView}
     */
    public void navigateTo(Class<? extends View> viewClass) {
        view.getUI().getNavigator().navigateTo(OrdersUI.getViewId(viewClass));
    }

    public void logout() {
        view.getUI().getSession().getSession().invalidate();
        String contextPath = ((VaadinServletService) VaadinServletService
                .getCurrent()).getServlet().getServletContext()
                        .getContextPath();
        view.getUI().getPage()
                .setLocation(contextPath + Application.LOGOUT_URL);
    }
}
