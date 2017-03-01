package com.vaadin.template.orders.ui.view.admin.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class UserAdminView extends AbstractCrudView<User> implements View {

    @Autowired
    private UserAdminPresenter presenter;

    private final UserAdminViewDesign userAdminViewDesign;

    public UserAdminView() {
        super(User.class);
        userAdminViewDesign = new UserAdminViewDesign();
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        getGrid().setColumns("email", "name", "role");
        getBinder().bindInstanceFields(getComponent());

        presenter.init(this);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Nothing to do here
    }

    @Override
    public UserAdminViewDesign getComponent() {
        return userAdminViewDesign;
    }

    @Override
    protected UserAdminPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Grid<User> getGrid() {
        return getComponent().list;
    }

    @Override
    protected void setGrid(Grid<User> grid) {
        getComponent().list = grid;
    }

    @Override
    protected Component getForm() {
        return getComponent().form;
    }

    @Override
    protected Button getAdd() {
        return getComponent().add;
    }

    @Override
    protected Button getCancel() {
        return getComponent().cancel;
    }

    @Override
    protected Button getDelete() {
        return getComponent().delete;
    }

    @Override
    protected Button getUpdate() {
        return getComponent().update;
    }

    @Override
    protected TextField getSearch() {
        return getComponent().search;
    }

}