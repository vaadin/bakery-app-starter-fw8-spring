package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.template.orders.app.Application;

@SpringComponent
public class MainPresenter {

    private MainView view;

    @Autowired
    public MainPresenter() {
    }

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

        SpringView springView = viewClass.getAnnotation(SpringView.class);
        if (springView == null) {
            throw new IllegalArgumentException(
                    "The target class must be a @SpringView");
        }

        String viewId = Conventions.deriveMappingForView(viewClass, springView);
        view.getUI().getNavigator().navigateTo(viewId);
    }

    public void logout() {
        view.getUI().getSession().getSession().invalidate();
        view.getUI().getPage().setLocation(Application.LOGOUT_URL);
    }
}
