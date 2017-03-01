package com.vaadin.template.orders.ui.view.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@PrototypeScope
public class UserAdminPresenter {

    private UserAdminView view;
    @Autowired
    private UserAdminDataProvider userAdminDataProvider;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public UserAdminDataProvider getDataProvider() {
        return userAdminDataProvider;
    }

    public void editRequest(User user) {
        // Fetch a fresh item so we have the latest changes (less optimistic
        // locking problems) and so that we are not editing the data shown in
        // the grid (change data -> cancel should not update the grid)
        User freshCopy = repository.findOne(user.getEmail());
        getView().editItem(freshCopy, false);
    }

    public String[] getAvailableRoles() {
        return Role.getAllRoles();
    }

    public void addNewClicked() {
        User user = new User("", "", "", Role.BARISTA);
        view.editItem(user, true);
    }

    public void updateClicked(boolean isNew) {
        User saved;
        try {
            saved = repository.save(view.getEditItem());
        } catch (Exception e) {
            // This could be either that somebody else edited the item -> should
            // tell to refresh
            // or then invalid data -> should not happen if validators are in
            // place
            Notification.show(
                    "A problem occured while saving the data. Please check the fields.",
                    Type.ERROR_MESSAGE);
            return;
        }
        if (isNew) {
            userAdminDataProvider.refreshAll();
        } else {
            userAdminDataProvider.refreshItem(saved);
        }
        view.stopEditing();
    }

    public void deleteClicked() {
        repository.delete(view.getEditItem().getEmail());
        userAdminDataProvider.refreshAll();
        view.stopEditing();
    }

    public void cancelClicked() {
        view.stopEditing();
    }

    public void filterGrid(String filter) {
        userAdminDataProvider.setFilter(filter);
    }

}
