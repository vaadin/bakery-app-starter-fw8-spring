package com.vaadin.template.orders.ui.view.admin.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.SingleSelectionModel;

@SpringView
@Secured(Role.ADMIN)
public class UserAdminView extends UserAdminViewDesign implements View {

    @Autowired
    private UserAdminPresenter presenter;

    private Binder<User> binder;

    @PostConstruct
    public void init() {
        presenter.init(this);

        // Because of https://github.com/vaadin/framework/issues/8467
        Grid<User> oldGrid = list;
        list = new Grid<>(User.class);
        list.setSizeFull();
        listParent.replaceComponent(oldGrid, list);
        // End of workaround

        list.setColumns("email", "name", "role");
        // Force user to choose save or cancel in form once enabled
        ((SingleSelectionModel<User>) list.getSelectionModel())
                .setDeselectAllowed(false);

        list.setDataProvider(presenter.getDataProvider());
        list.addSelectionListener(e -> {
            if (!e.isUserOriginated()) {
                return;
            }

            if (e.getFirstSelectedItem().isPresent()) {
                presenter.editRequest(e.getFirstSelectedItem().get());
            } else {
                throw new IllegalStateException(
                        "This should never happen as deselection is not allowed");
            }
        });

        role.setItems(presenter.getAvailableRoles());
        binder = new Binder<>(User.class);
        binder.bindInstanceFields(this);

        update.addClickListener(event -> {
            boolean isNew = list.getSelectedItems().isEmpty();
            presenter.updateClicked(isNew);
        });
        delete.addClickListener(event -> presenter.deleteClicked());
        cancel.addClickListener(event -> presenter.cancelClicked());
        add.addClickListener(event -> presenter.addNewClicked());

        search.addValueChangeListener(
                event -> presenter.filterGrid(event.getValue()));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

    public void editItem(User user, boolean isNew) {
        binder.setBean(user);
        form.setEnabled(true);
        add.setEnabled(false);
        delete.setEnabled(!isNew);
        update.setCaption(isNew ? "Add" : "Update");
    }

    public User getEditItem() {
        return binder.getBean();
    }

    public void stopEditing() {
        form.setEnabled(false);
        add.setEnabled(true);
        binder.setBean(null);
        list.deselectAll();
    }
}