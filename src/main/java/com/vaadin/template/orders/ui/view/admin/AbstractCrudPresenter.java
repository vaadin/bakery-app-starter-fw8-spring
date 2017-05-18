package com.vaadin.template.orders.ui.view.admin;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.data.HasValue;
import com.vaadin.template.orders.ui.HasLogger;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public abstract class AbstractCrudPresenter<T extends Serializable, V extends AbstractCrudView<T>>
		implements HasLogger, Serializable {

	private V view;

	public abstract void filterGrid(String filter);

	protected abstract T getCopy(T entity);

	protected abstract PageableDataProvider<T, Object> getGridDataProvider();

	protected abstract T createEntity();

	protected abstract boolean isNew(T entity);

	protected abstract T saveEntity(T editItem);

	protected abstract void deleteEntity(T entity);

	public void init(V view) {
		this.view = view;
		view.showInitialState();
	}

	protected V getView() {
		return view;
	}

	public void editRequest(T entity) {
		// Fetch a fresh item so we have the latest changes (less optimistic
		// locking problems)
		T freshCopy = getCopy(entity);
		getView().editItem(freshCopy, false);
	}

	public void addNewClicked() {
		T entity = createEntity();
		getView().editItem(entity, true);
	}

	public void updateClicked() {
		Optional<HasValue<?>> firstErrorField = view.validate().findFirst();
		if (firstErrorField.isPresent()) {
			((Focusable) firstErrorField.get()).focus();
			return;
		}

		if (!view.commitEditItem()) {
			// Commit failed because of validation errors - which should never
			// happen as validation is checked above
			Notification.show(
					"An unexpected problem occured while saving the data. Please try refreshing the view or contact the administrator.",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to commit entity of type " + view.getEditItem().getClass().getName());
			return;
		}

		T entity = view.getEditItem();
		boolean isNew = isNew(entity);
		try {
			entity = saveEntity(entity);
		} catch (Exception e) {
			// The most likely cause is an optimistic locking error, i.e.
			// somebody else edited the data
			Notification.show("A problem occured while saving the data. Please check the fields.", Type.ERROR_MESSAGE);
			getLogger().error("Unable to save entity of type " + entity.getClass().getName(), e);
			return;
		}

		if (isNew) {
			// Move to the "Updating an entity" state
			getGridDataProvider().refreshAll();
			getView().getGrid().select(entity);
			editRequest(entity);
		} else {
			// Stay in the "Updating an entity" state
			getGridDataProvider().refreshItem(entity);
			editRequest(entity);
		}
	}

	public void cancelClicked() {
		T entity = getView().getEditItem();
		if (isNew(entity)) {
			getView().showInitialState();
		} else {
			getView().editItem(entity, false);
		}
	}

	public void deleteClicked() {
		T entity = getView().getEditItem();
		try {
			deleteEntity(entity);
		} catch (DataIntegrityViolationException e) {
			Notification.show("The given entity cannot be deleted as there are references to it in the database",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to delete entity of type " + entity.getClass().getName(), e);
			return;
		}
		getGridDataProvider().refreshAll();
		// getView().stopEditing();
	}

	public void formStatusChanged(boolean hasValidationErrors, boolean hasChanges) {
		getView().getUpdate().setEnabled(hasChanges && !hasValidationErrors);
		getView().getCancel().setEnabled(hasChanges);
	}
}
