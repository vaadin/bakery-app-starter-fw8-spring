package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudPresenter;

@SpringComponent
@PrototypeScope
public class ProductAdminPresenter
        extends AbstractCrudPresenter<Product, ProductAdminView> {

    @Autowired
    private ProductAdminDataProvider userAdminDataProvider;
    @Autowired
    private ProductRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected Product getCopy(Product entity) {
        return repository.findOne(entity.getId());
    }

    @Override
    protected ProductAdminDataProvider getGridDataProvider() {
        return userAdminDataProvider;
    }

    @Override
    protected void deleteEntity(Product entity) {
        repository.delete(entity.getId());
    }

    @Override
    protected Product saveEntity(Product editItem) {
        return repository.save(editItem);
    }

    @Override
    protected Product createEntity() {
        return new Product();
    }

    @Override
    public void filterGrid(String filter) {
        getGridDataProvider().setFilter(filter);
    }

}
