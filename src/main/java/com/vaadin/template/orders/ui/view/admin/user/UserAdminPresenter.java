package com.vaadin.template.orders.ui.view.admin.user;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudPresenter;

@SpringComponent
@PrototypeScope
public class UserAdminPresenter extends AbstractCrudPresenter<User, UserAdminView> implements Serializable {

	@Autowired
	private UserAdminDataProvider userAdminDataProvider;

	private transient UserService service;

	@Override
	protected User getCopy(User entity) {
		return getUserService().get(entity.getId());
	}

	@Override
	protected UserAdminDataProvider getGridDataProvider() {
		return userAdminDataProvider;
	}

	@Override
	protected void deleteEntity(User entity) {
		getUserService().delete(entity.getId());
	}

	@Override
	protected User saveEntity(User editItem) {
		return getUserService().save(editItem);
	}

	@Override
	protected User createEntity() {
		return new User("", "", "", Role.BARISTA);
	}

	@Override
	public void filterGrid(String filter) {
		getGridDataProvider().setFilter(filter);
	}

	protected UserService getUserService() {
		return service = BeanLocator.use(service).orElseFindInstance(UserService.class);
	}
}
