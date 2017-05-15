package com.vaadin.template.orders.ui.components;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.service.ProductService;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
public class ProductComboBoxDataProvider extends PageableDataProvider<Product, String> {

	private transient ProductService productService;

	@Override
	protected Page<Product> fetchFromBackEnd(Query<Product, String> query, Pageable pageable) {
		return getProductService().findAnyMatching(query.getFilter(), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<Product, String> query) {
		return (int) getProductService().countAnyMatching(query.getFilter());
	}

	protected ProductService getProductService() {
		return productService = BeanLocator.use(productService).orElseFindInstance(ProductService.class);
	}
}
