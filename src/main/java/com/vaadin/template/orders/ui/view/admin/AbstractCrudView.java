package com.vaadin.template.orders.ui.view.admin;

import java.io.Serializable;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.Binder;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.SingleSelectionModel;

@Secured(Role.ADMIN)
public abstract class AbstractCrudView<T> implements Serializable {

	private final Class<T> entityType;
	private final Binder<T> binder;

	protected AbstractCrudView(Class<T> entityType) {
		this.entityType = entityType;
		this.binder = new Binder<>(entityType);
	}

	public void editItem(T entity, boolean isNew) {
		getBinder().setBean(entity);
		getForm().setEnabled(true);
		getAdd().setEnabled(false);
		getDelete().setEnabled(!isNew);
		getUpdate().setCaption(isNew ? "Add" : "Update");
	}

	public T getEditItem() {
		return getBinder().getBean();
	}

	protected Binder<T> getBinder() {
		return binder;
	}

	public void stopEditing() {
		getForm().setEnabled(false);
		getAdd().setEnabled(true);
		getBinder().setBean(null);
		getGrid().deselectAll();
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
		getGrid().setDataProvider(getPresenter().getGridDataProvider());

		// Button logic
		getUpdate().addClickListener(event -> {
			boolean isNew = getGrid().getSelectedItems().isEmpty();
			getPresenter().updateClicked(isNew);
		});
		getDelete().addClickListener(event -> getPresenter().deleteClicked());
		getCancel().addClickListener(event -> getPresenter().cancelClicked());
		getAdd().addClickListener(event -> getPresenter().addNewClicked());

		// Search functionality
		getSearch().addValueChangeListener(event -> getPresenter().filterGrid(event.getValue()));

	}

	protected abstract AbstractCrudPresenter<T, ? extends AbstractCrudView<T>> getPresenter();

	protected abstract Grid<T> getGrid();

	protected abstract void setGrid(Grid<T> grid);

	protected abstract Component getForm();

	protected abstract Button getAdd();

	protected abstract Button getCancel();

	protected abstract Button getDelete();

	protected abstract Button getUpdate();

	protected abstract TextField getSearch();

}
