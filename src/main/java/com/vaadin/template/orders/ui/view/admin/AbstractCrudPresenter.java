package com.vaadin.template.orders.ui.view.admin;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.data.HasValue;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.AbstractEntity;
import com.vaadin.template.orders.backend.service.CrudService;
import com.vaadin.template.orders.ui.HasLogger;
import com.vaadin.template.orders.ui.NavigationManager;
import com.vaadin.template.orders.ui.components.ConfirmationDialog;
import com.vaadin.template.orders.ui.dataprovider.PageableDataProvider;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public abstract class AbstractCrudPresenter<T extends AbstractEntity, S extends CrudService<T>, V extends AbstractCrudView<T>>
		implements HasLogger, Serializable {

	private V view;

	private final NavigationManager navigationManager;

	private final Class<S> serviceType;

	@Autowired
	protected AbstractCrudPresenter(NavigationManager navigationManager) {
		this.serviceType = discoverServiceType();
		this.navigationManager = navigationManager;
	}

	@SuppressWarnings("unchecked")
	private Class<S> discoverServiceType() {
		ResolvableType forClass = ResolvableType.forClass(getClass());
		ResolvableType resolvableGeneric = forClass.getSuperType().getGeneric(1);
		return (Class<S>) resolvableGeneric.resolve();
	}

	protected S getService() {
		return BeanLocator.find(serviceType);
	}

	public abstract void filterGrid(String filter);

	protected T loadEntity(long id) {
		return getService().load(id);
	}

	protected abstract PageableDataProvider<T, Object> getGridDataProvider();

	protected abstract T createEntity();

	protected T saveEntity(T editItem) {
		return getService().save(editItem);
	}

	protected void deleteEntity(T entity) {
		if (entity.isPersisted()) {
			getService().delete(entity.getId());
		} else {
			throw new IllegalArgumentException("Cannot delete an entity which is not in the database");
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
			editItem(loadEntity(getId(entity)));
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
		boolean isNew = isNew(item);
		if (isNew) {
			navigationManager.updateViewParameter("new");
		} else {
			Long id = getId(item);
			navigationManager.updateViewParameter(String.valueOf(id));
		}
		getView().editItem(item, isNew);
	}

	public void addNewClicked() {
		runIfNoUnsavedChanges(() -> {
			T entity = createEntity();
			editItem(entity);
		});
	}

	/**
	 * Runs the given command if the form contains no unsaved changes or if the
	 * user clicks ok in the confirmation dialog telling about unsaved changes.
	 *
	 * @param onOk
	 *            the command to run
	 */
	private void runIfNoUnsavedChanges(Runnable onOk) {
		runIfNoUnsavedChanges(onOk, () -> {
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
			ConfirmationDialog.show(getView().getViewComponent().getUI(), onOk, onCancel);
		} else {
			onOk.run();
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
			selectAndEditEntity(entity);
		} else {
			// Stay in the "Updating an entity" state
			getGridDataProvider().refreshItem(entity);
			editRequest(entity);
		}
	}

	public void cancelClicked() {
		T entity = getView().getEditItem();
		if (isNew(entity)) {
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
		getGridDataProvider().refreshAll();
		showInitialState();
	}

	public void formStatusChanged(boolean hasValidationErrors, boolean hasChanges) {
		getView().getUpdate().setEnabled(hasChanges && !hasValidationErrors);
		getView().getCancel().setEnabled(hasChanges);
	}

	protected boolean isNew(T item) {
		return getId(item) == null;
	}

	protected Long getId(AbstractEntity entity) {
		return entity.getId();
	}

}
