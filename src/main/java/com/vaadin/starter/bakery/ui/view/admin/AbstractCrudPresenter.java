package com.vaadin.starter.bakery.ui.view.admin;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.core.ResolvableType;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.data.HasValue;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public abstract class AbstractCrudPresenter<T extends AbstractEntity, S extends CrudService<T>, V extends AbstractCrudView<T>>
		implements HasLogger, Serializable {

	private V view;

	private final NavigationManager navigationManager;

	private final S service;

	private FilterablePageableDataProvider<T, Object> dataProvider;

	protected AbstractCrudPresenter(NavigationManager navigationManager, S service,
			FilterablePageableDataProvider<T, Object> dataProvider) {
		this.service = service;
		this.navigationManager = navigationManager;
		this.dataProvider = dataProvider;
	}

	protected S getService() {
		return service;
	}

	protected void filterGrid(String filter) {
		getDataProvider().setFilter(filter);
	}

	protected T loadEntity(long id) {
		return service.load(id);
	}

	protected FilterablePageableDataProvider<T, Object> getDataProvider() {
		return dataProvider;
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityType() {
		return (Class<T>) ResolvableType.forClass(getClass()).getSuperType().getGeneric(0).resolve();
	}

	protected T createEntity() {
		try {
			return getEntityType().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UnsupportedOperationException(
					"Entity of type " + getEntityType().getName() + " is missing a public no-args constructor", e);
		}
	}

	protected T saveEntity(T editItem) {
		return service.save(editItem);
	}

	protected void deleteEntity(T entity) {
		if (entity.isNew()) {
			throw new IllegalArgumentException("Cannot delete an entity which is not in the database");
		} else {
			service.delete(entity.getId());
		}
	}

	public void init(V view) {
		this.view = view;
		view.showInitialState();
	}

	protected V getView() {
		return view;
	}

	public void editRequest(String parameters) {
		long id;
		try {
			id = Long.parseLong(parameters);
		} catch (NumberFormatException e) {
			id = -1;
		}

		if (id == -1) {
			editItem(createEntity());
		} else {
			selectAndEditEntity(loadEntity(id));
		}
	}

	private void selectAndEditEntity(T entity) {
		getView().getGrid().select(entity);
		editRequest(entity);
	}

	public void editRequest(T entity) {
		runIfNoUnsavedChanges(() -> {
			// Fetch a fresh item so we have the latest changes (less optimistic
			// locking problems)
			T freshEntity = loadEntity(entity.getId());
			editItem(freshEntity);
		}, () -> {
			// Revert selection in grid
			T editItem = getView().getEditItem();
			Grid<T> grid = getView().getGrid();
			if (editItem == null) {
				grid.deselectAll();
			} else {
				grid.select(editItem);
			}
		});
	}

	protected void editItem(T item) {
		boolean isNew = item.isNew();
		if (isNew) {
			navigationManager.updateViewParameter("new");
		} else {
			navigationManager.updateViewParameter(String.valueOf(item.getId()));
		}
		getView().editItem(item, isNew);
	}

	public void addNewClicked() {
		runIfNoUnsavedChanges(() -> {
			T entity = createEntity();
			editItem(entity);
		}, () -> {
		});
	}

	/**
	 * Runs the given command if the form contains no unsaved changes or if the
	 * user clicks ok in the confirmation dialog telling about unsaved changes.
	 *
	 * @param onOk
	 *            the command to run if there are not changes or user pushes ok
	 * @param onCancel
	 *            the command to run if there are changes and the user pushes
	 *            cancel
	 */
	private void runIfNoUnsavedChanges(Runnable onOk, Runnable onCancel) {
		if (view.containsUnsavedChanges()) {
			getView().showLeaveViewConfirmDialog(onOk, onCancel);
		} else {
			UI.getCurrent().access(onOk);
		}
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
		boolean isNew = entity.isNew();
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
			getDataProvider().refreshAll();
			selectAndEditEntity(entity);
		} else {
			// Stay in the "Updating an entity" state
			getDataProvider().refreshItem(entity);
			editRequest(entity);
		}
	}

	public void cancelClicked() {
		T entity = getView().getEditItem();
		if (entity.isNew()) {
			showInitialState();
		} else {
			editItem(entity);
		}
	}

	private void showInitialState() {
		getView().showInitialState();
		navigationManager.updateViewParameter("");
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
		getDataProvider().refreshAll();
		showInitialState();
	}

	public void formStatusChanged(boolean hasValidationErrors, boolean hasChanges) {
		getView().getUpdate().setEnabled(hasChanges && !hasValidationErrors);
		getView().getCancel().setEnabled(hasChanges);
	}
}
