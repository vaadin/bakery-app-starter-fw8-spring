package com.vaadin.starter.bakery.ui.view.admin;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.ResolvableType;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.ui.components.ConfirmPopup;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public abstract class AbstractCrudPresenter<T extends AbstractEntity, S extends CrudService<T>, V extends AbstractCrudView<T>>
		implements HasLogger, Serializable {

	private V view;

	private final NavigationManager navigationManager;

	private final S service;

	private FilterablePageableDataProvider<T, Object> dataProvider;

	private BeanValidationBinder<T> binder;

	// The model for the view. Not extracted to a class to reduce clutter. If
	// the model becomes more complex, it could be encapsulated in a separate
	// class.
	private T editItem;

	protected AbstractCrudPresenter(NavigationManager navigationManager, S service,
			FilterablePageableDataProvider<T, Object> dataProvider) {
		this.service = service;
		this.navigationManager = navigationManager;
		this.dataProvider = dataProvider;
		createBinder();
	}

	public void viewEntered(ViewChangeEvent event) {
		if (!event.getParameters().isEmpty()) {
			editRequest(event.getParameters());
		}
	}

	public boolean beforeLeavingView(Runnable runOnLeave) {
		return runWithConfirmation(runOnLeave, () -> {
			// Nothing special needs to be done if user aborts the navigation
		});
	}

	protected void createBinder() {
		binder = new BeanValidationBinder<>(getEntityType());
		binder.addStatusChangeListener(this::onFormStatusChange);
	}

	protected BeanValidationBinder<T> getBinder() {
		return binder;
	}

	protected S getService() {
		return service;
	}

	protected void filterGrid(String filter) {
		dataProvider.setFilter(filter);
	}

	protected T loadEntity(long id) {
		return service.load(id);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityType() {
		return (Class<T>) ResolvableType.forClass(getClass()).getSuperType().getGeneric(0).resolve();
	}

	private T createEntity() {
		try {
			return getEntityType().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UnsupportedOperationException(
					"Entity of type " + getEntityType().getName() + " is missing a public no-args constructor", e);
		}
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
		view.setDataProvider(dataProvider);
		view.bindFormFields(getBinder());
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
		runWithConfirmation(() -> {
			// Fetch a fresh item so we have the latest changes (less optimistic
			// locking problems)
			T freshEntity = loadEntity(entity.getId());
			editItem(freshEntity);
		}, () -> {
			// Revert selection in grid
			Grid<T> grid = getView().getGrid();
			if (editItem == null) {
				grid.deselectAll();
			} else {
				grid.select(editItem);
			}
		});
	}

	protected void editItem(T item) {
		if (item == null) {
			throw new IllegalArgumentException("The entity to edit cannot be null");
		}
		this.editItem = item;

		boolean isNew = item.isNew();
		if (isNew) {
			navigationManager.updateViewParameter("new");
		} else {
			navigationManager.updateViewParameter(String.valueOf(item.getId()));
		}

		getBinder().readBean(editItem);
		getView().editItem(isNew);
	}

	public void addNewClicked() {
		runWithConfirmation(() -> {
			T entity = createEntity();
			editItem(entity);
		}, () -> {
		});
	}

	/**
	 * Runs the given command if the form contains no unsaved changes or if the
	 * user clicks ok in the confirmation dialog telling about unsaved changes.
	 *
	 * @param onConfirmation
	 *            the command to run if there are not changes or user pushes
	 *            {@literal confirm}
	 * @param onCancel
	 *            the command to run if there are changes and the user pushes
	 *            {@literal cancel}
	 * @return <code>true</code> if the {@literal confirm} command was run
	 *         immediately, <code>false</code> otherwise
	 */
	private boolean runWithConfirmation(Runnable onConfirmation, Runnable onCancel) {
		boolean hasUnsavedChanges = editItem != null && getBinder().hasChanges();

		if (hasUnsavedChanges) {
			ConfirmPopup.get().showLeaveViewConfirmDialog(view, onConfirmation, onCancel);
			return false;
		} else {
			onConfirmation.run();
			return true;
		}
	}

	public void updateClicked() {
		BinderValidationStatus<T> validationStatus = binder.validate();
		if (validationStatus.hasErrors()) {
			Stream<HasValue<?>> fieldsWithErrors = validationStatus.getFieldValidationErrors().stream()
					.map(BindingValidationStatus::getField);
			Optional<HasValue<?>> firstErrorField = fieldsWithErrors.findFirst();
			if (firstErrorField.isPresent()) {
				getView().focusField(firstErrorField.get());
			} else {
				// Bean validation error
			}

			return;
		}

		if (!getBinder().writeBeanIfValid(editItem)) {
			// Commit failed because of validation errors - which should never
			// happen as validation is checked above
			Notification.show(
					"An unexpected problem occured while saving the data. Please try refreshing the view or contact the administrator.",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to commit entity of type " + editItem.getClass().getName());
			return;
		}

		boolean isNew = editItem.isNew();
		T entity;
		try {
			entity = service.save(editItem);
		} catch (Exception e) {
			// The most likely cause is an optimistic locking error, i.e.
			// somebody else edited the data
			Notification.show("A problem occured while saving the data. Please check the fields.", Type.ERROR_MESSAGE);
			getLogger().error("Unable to save entity of type " + editItem.getClass().getName(), e);
			return;
		}

		if (isNew) {
			// Move to the "Updating an entity" state
			dataProvider.refreshAll();
			selectAndEditEntity(entity);
		} else {
			// Stay in the "Updating an entity" state
			dataProvider.refreshItem(entity);
			editRequest(entity);
		}
	}

	public void cancelClicked() {
		if (editItem.isNew()) {
			revertToInitialState();
		} else {
			editItem(editItem);
		}
	}

	private void revertToInitialState() {
		editItem = null;
		getBinder().readBean(null);
		getView().showInitialState();
		navigationManager.updateViewParameter("");
	}

	public void deleteClicked() {
		try {
			deleteEntity(editItem);
		} catch (DataIntegrityViolationException e) {
			Notification.show("The given entity cannot be deleted as there are references to it in the database",
					Type.ERROR_MESSAGE);
			getLogger().error("Unable to delete entity of type " + editItem.getClass().getName(), e);
			return;
		}
		dataProvider.refreshAll();
		revertToInitialState();
	}

	public void onFormStatusChange(StatusChangeEvent event) {
		boolean hasChanges = event.getBinder().hasChanges();
		boolean hasValidationErrors = event.hasValidationErrors();
		getView().setUpdateEnabled(hasChanges && !hasValidationErrors);
		getView().setCancelEnabled(hasChanges);
	}

}
