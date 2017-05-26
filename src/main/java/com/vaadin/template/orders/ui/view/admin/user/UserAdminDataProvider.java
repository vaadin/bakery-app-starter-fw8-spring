package com.vaadin.template.orders.ui.view.admin.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.FilterablePageableDataProvider;

@SpringComponent
@PrototypeScope
public class UserAdminDataProvider extends FilterablePageableDataProvider<User, Object> {

	private transient UserService userService;

	@Override
	protected Page<User> fetchFromBackEnd(Query<User, Object> query, Pageable pageable) {
		return getUserService().findAnyMatching(getOptionalFilter(), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<User, Object> query) {
		return (int) getUserService().countAnyMatching(getOptionalFilter());
	}

	@Override
	public Object getId(User item) {
		return item.getId();
	}

	protected UserService getUserService() {
		if (userService == null) {
			userService = BeanLocator.find(UserService.class);
		}
		return userService;
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("email", SortDirection.ASCENDING));
		return sortOrders;
	}

}