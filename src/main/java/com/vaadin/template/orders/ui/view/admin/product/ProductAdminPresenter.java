package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.service.ProductService;
import com.vaadin.template.orders.ui.navigation.NavigationManager;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudPresenter;

@SpringComponent
@ViewScope
public class ProductAdminPresenter extends AbstractCrudPresenter<Product, ProductService, ProductAdminView> {

	private final ProductAdminDataProvider productAdminDataProvider;

	@Autowired
	public ProductAdminPresenter(ProductAdminDataProvider productAdminDataProvider,
			NavigationManager navigationManager) {
		super(navigationManager);
		this.productAdminDataProvider = productAdminDataProvider;
	}

	@Override
	protected ProductAdminDataProvider getGridDataProvider() {
		return productAdminDataProvider;
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
