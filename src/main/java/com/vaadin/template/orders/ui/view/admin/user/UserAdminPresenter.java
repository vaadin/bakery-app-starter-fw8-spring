package com.vaadin.template.orders.ui.view.admin.user;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudPresenter;

@SpringComponent
@PrototypeScope
public class UserAdminPresenter extends AbstractCrudPresenter<User, UserAdminView> implements Serializable {

	@Autowired
	private UserAdminDataProvider userAdminDataProvider;
	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected User getCopy(User entity) {
		return repository.findOne(entity.getId());
	}

	@Override
	protected UserAdminDataProvider getGridDataProvider() {
		return userAdminDataProvider;
	}

	@Override
	protected void deleteEntity(User entity) {
		repository.delete(entity.getId());
	}

	@Override
	protected User saveEntity(User editItem) {
		return repository.save(editItem);
	}

	@Override
	protected User createEntity() {
		return new User("", "", "", Role.BARISTA);
	}

	@Override
	public void filterGrid(String filter) {
		getGridDataProvider().setFilter(filter);
	}

}
