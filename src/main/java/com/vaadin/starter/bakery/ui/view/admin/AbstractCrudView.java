package com.vaadin.starter.bakery.ui.view.admin;

import java.io.Serializable;
import java.util.stream.Stream;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.view.NavigableView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.SingleSelectionModel;

/**
 * Base class for a CRUD (Create, read, update, delete) view.
 * <p>
 * The view has three states it can be in and the user can navigate between the
 * states with the controls present:
 * <ol>
 * <li>Initial state
 * <ul>
 * <li>Form is disabled
 * <li>Nothing is selected in grid
 * </ul>
 * <li>Adding an entity
 * <ul>
 * <li>Form is enabled
 * <li>"Delete" has no function
 * <li>"Discard" moves to the "Initial state"
 * <li>"Save" creates the entity and moves to the "Updating an entity" state
 * </ul>
 * <li>Updating an entity
 * <ul>
 * <li>Entity highlighted in Grid
 * <li>Form is enabled
 * <li>"Delete" deletes the entity from the database
 * <li>"Discard" resets the form contents to what is in the database
 * <li>"Save" updates the entity and keeps the form open
 * <li>"Save" and "Discard" are only enabled when changes have been made
 * </ol>
 *
 * @param <T>
 *            the type of entity which can be edited in the view
 */
@Secured(Role.ADMIN)
public abstract class AbstractCrudView<T extends AbstractEntity> implements Serializable, NavigableView {

	public static final String CAPTION_DISCARD = "Discard";
	public static final String CAPTION_CANCEL = "Cancel";
	public static final String CAPTION_UPDATE = "Update";
	public static final String CAPTION_ADD = "Add";
	private final BeanValidationBinder<T> binder;
	private T editItem;

	protected AbstractCrudView(Class<T> entityType) {
		this.binder = new BeanValidationBinder<>(entityType);
		getBinder().addStatusChangeListener(
				statusChange -> getPresenter().formStatusChanged(statusChange.hasValidationErrors(), isFormModified()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (!event.getParameters().isEmpty()) {
			getPresenter().editRequest(event.getParameters());
		}
	}

	public void showInitialState() {
		this.editItem = null;
		getForm().setEnabled(false);
		getGrid().deselectAll();
		getBinder().readBean(null);
		getUpdate().setCaption(CAPTION_UPDATE);
		getCancel().setCaption(CAPTION_DISCARD);
	}

	public void editItem(T editItem, boolean isNew) {
		if (editItem == null) {
			throw new IllegalArgumentException("The entity to edit cannot be null");
		}
		this.editItem = editItem;
		if (isNew) {
			getGrid().deselectAll();
			getUpdate().setCaption(CAPTION_ADD);
			getCancel().setCaption(CAPTION_CANCEL);
			getFirstFormField().focus();
		} else {
			getUpdate().setCaption(CAPTION_UPDATE);
			getCancel().setCaption(CAPTION_DISCARD);
		}

		getBinder().readBean(editItem);
		getForm().setEnabled(true);
		getDelete().setEnabled(!isNew);
	}

	private boolean isFormModified() {
		return getBinder().hasChanges();
	}

	public boolean containsUnsavedChanges() {
		return editItem != null && isFormModified();
	}

	public Stream<HasValue<?>> validate() {
		return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
	}

	public T getEditItem() {
		return editItem;
	}

	public boolean commitEditItem() {
		return getBinder().writeBeanIfValid(editItem);
	}

	protected Binder<T> getBinder() {
		return binder;
	}

	protected void init() {
		getGrid().addSelectionListener(e -> {
			if (!e.isUserOriginated()) {
				return;
			}

			if (e.getFirstSelectedItem().isPresent()) {
				getPresenter().editRequest(e.getFirstSelectedItem().get());
			} else {
				throw new IllegalStateException("This should never happen as deselection is not allowed");
			}
		});

		// Force user to choose save or cancel in form once enabled
		((SingleSelectionModel<T>) getGrid().getSelectionModel()).setDeselectAllowed(false);
		getGrid().setDataProvider(getPresenter().getDataProvider());

		// Button logic
		getUpdate().addClickListener(event -> getPresenter().updateClicked());
		getCancel().addClickListener(event -> getPresenter().cancelClicked());
		getDelete().addClickListener(event -> getPresenter().deleteClicked());
		getAdd().addClickListener(event -> getPresenter().addNewClicked());

		// Search functionality
		getSearch().addValueChangeListener(event -> getPresenter().filterGrid(event.getValue()));

	}

	protected abstract AbstractCrudPresenter<T, ?, ? extends AbstractCrudView<T>> getPresenter();

	protected abstract Grid<T> getGrid();

	protected abstract void setGrid(Grid<T> grid);

	protected abstract Component getForm();

	protected abstract Button getAdd();

	protected abstract Button getCancel();

	protected abstract Button getDelete();

	protected abstract Button getUpdate();

	protected abstract TextField getSearch();

	protected abstract Focusable getFirstFormField();

	@Override
	public boolean beforeLeave(Runnable runOnLeave) {
		if (containsUnsavedChanges()) {
			showLeaveViewConfirmDialog(runOnLeave);
			return false;
		} 

		return true;
	}
}
