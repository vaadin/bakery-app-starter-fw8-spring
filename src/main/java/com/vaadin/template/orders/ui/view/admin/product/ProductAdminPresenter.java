package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.service.ProductService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudPresenter;

@SpringComponent
@PrototypeScope
public class ProductAdminPresenter extends AbstractCrudPresenter<Product, ProductAdminView> {

	@Autowired
	private ProductAdminDataProvider userAdminDataProvider;

	private transient ProductService productService;

	@Override
	protected Product loadEntity(Long id) {
		return getService().get(id);
	}

	@Override
	protected ProductAdminDataProvider getGridDataProvider() {
		return userAdminDataProvider;
	}

	@Override
	protected void deleteEntity(Product entity) {
		getService().delete(entity.getId());
	}

	@Override
	protected Product saveEntity(Product editItem) {
		return getService().save(editItem);
	}

	@Override
	protected Product createEntity() {
		return new Product();
	}

	@Override
	public void filterGrid(String filter) {
		getGridDataProvider().setFilter(filter);
	}

	protected ProductService getService() {
		if (productService == null) {
			productService = BeanLocator.find(ProductService.class);
		}
		return productService;
	}

}
