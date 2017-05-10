package com.vaadin.template.orders.ui.view.admin.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class UserAdminDataProvider extends PageableDataProvider<User, Object> {

	@Autowired
	private UserService service;
	private Optional<String> filter = Optional.empty();

	@Override
	protected Page<User> fetchFromBackEnd(Query<User, Object> query, Pageable pageable) {
		return service.findAnyMatching(filter, pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<User, Object> query) {
		if (filter == null) {
			return (int) service.count();
		} else {
			return (int) service.countAnyMatching(filter);
		}
	}

	public void setFilter(String filter) {
		if ("".equals(filter)) {
			this.filter = Optional.empty();
		} else {
			this.filter = Optional.of(filter);
		}
		refreshAll();
	}

	@Override
	public Object getId(User item) {
		return item.getId();
	}

}