package com.vaadin.template.orders.ui.view.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class UserAdminDataProvider extends PageableDataProvider<User, Object> {

    @Autowired
    private UserRepository repository;
    private String filter = null;

    @Override
    protected Page<User> fetchFromBackEnd(Query<User, Object> query,
            Pageable pageable) {
        if (filter == null) {
            return repository.findByOrderByEmail(pageable);
        } else {
            return repository
                    .findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrderByEmail(
                            filter, filter, pageable);
        }
    }

    @Override
    protected int sizeInBackEnd(Query<User, Object> query) {
        if (filter == null) {
            return (int) repository.count();
        } else {
            return (int) repository
                    .countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(filter,
                            filter);
        }
    }

    public void setFilter(String filter) {
        if ("".equals(filter)) {
            this.filter = null;
        } else {
            this.filter = "%" + filter + "%";
        }
        refreshAll();
    }

    @Override
    public Object getId(User item) {
        return item.getEmail();
    }

}