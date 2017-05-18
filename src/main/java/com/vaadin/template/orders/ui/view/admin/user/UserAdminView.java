package com.vaadin.template.orders.ui.view.admin.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class UserAdminView extends AbstractCrudView<User> {

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
		presenter.init(this);
		getGrid().setColumns("email", "name", "role");
		getBinder().bind(getViewComponent().password, bean -> "", (bean, value) -> {
			if (value.isEmpty()) {
				// If nothing is entered in the password field, do nothing
			} else {
				bean.setPassword(presenter.encodePassword(value));
			}
		});
		getBinder().bindInstanceFields(getViewComponent());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Nothing to do here
	}

	@Override
	public UserAdminViewDesign getViewComponent() {
		return userAdminViewDesign;
	}

	@Override
	protected UserAdminPresenter getPresenter() {
		return presenter;
	}

	@Override
	protected Grid<User> getGrid() {
		return getViewComponent().list;
	}

	@Override
	protected void setGrid(Grid<User> grid) {
		getViewComponent().list = grid;
	}

	@Override
	protected Component getForm() {
		return getViewComponent().form;
	}

	@Override
	protected Button getAdd() {
		return getViewComponent().add;
	}

	@Override
	protected Button getCancel() {
		return getViewComponent().cancel;
	}

	@Override
	protected Button getDelete() {
		return getViewComponent().delete;
	}

	@Override
	protected Button getUpdate() {
		return getViewComponent().update;
	}

	@Override
	protected TextField getSearch() {
		return getViewComponent().search;
	}

	@Override
	protected Focusable getFirstFormField() {
		return getViewComponent().email;
	}

}
