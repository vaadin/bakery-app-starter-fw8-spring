package com.vaadin.template.orders.ui.view.admin;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public abstract class AbstractCrudPresenter<T, V extends AbstractCrudView<T>>
        implements Serializable {

    private V view;

    public abstract void filterGrid(String filter);

    protected abstract T getCopy(T entity);

    protected abstract PageableDataProvider<T, Object> getGridDataProvider();

    protected abstract T createEntity();

    protected abstract T saveEntity(T editItem);

    protected abstract void deleteEntity(T entity);

    public void init(V view) {
        this.view = view;
    }

    protected V getView() {
        return view;
    }

    public void editRequest(T entity) {
        // Fetch a fresh item so we have the latest changes (less optimistic
        // locking problems) and so that we are not editing the data shown in
        // the grid (change data -> cancel should not update the grid)
        T freshCopy = getCopy(entity);
        getView().editItem(freshCopy, false);
    }

    public void addNewClicked() {
        T entity = createEntity();
        getView().editItem(entity, true);
    }

    public void updateClicked(boolean isNew) {
        T entity = view.getEditItem();
        try {
            entity = saveEntity(entity);
        } catch (Exception e) {
            // This could be either that somebody else edited the item -> should
            // tell to refresh
            // or then invalid data -> should not happen if validators are in
            // place
            Notification.show(
                    "A problem occured while saving the data. Please check the fields.",
                    Type.ERROR_MESSAGE);
            getLogger().log(Level.FINE, "Unable to save entity of type "
                    + entity.getClass().getName(), e);
            return;
        }
        if (isNew) {
            getGridDataProvider().refreshAll();
        } else {
            getGridDataProvider().refreshItem(entity);
        }
        view.stopEditing();
    }

    public void deleteClicked() {
        T entity = getView().getEditItem();
        try {
            deleteEntity(entity);
        } catch (DataIntegrityViolationException e) {
            Notification.show(
                    "The given entity cannot be deleted as there are references to it in the database",
                    Type.ERROR_MESSAGE);
            getLogger().log(Level.FINE, "Unable to delete entity of type "
                    + entity.getClass().getName(), e);
            return;
        }
        getGridDataProvider().refreshAll();
        getView().stopEditing();
    }

    private Logger getLogger() {
        return Logger.getLogger(AbstractCrudPresenter.class.getName());
    }

    public void cancelClicked() {
        getView().stopEditing();
    }

}
