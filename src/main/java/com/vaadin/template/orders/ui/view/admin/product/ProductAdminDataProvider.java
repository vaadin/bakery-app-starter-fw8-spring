package com.vaadin.template.orders.ui.view.admin.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.service.ProductService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class ProductAdminDataProvider extends PageableDataProvider<Product, Object> {

	private transient ProductService service;

	private Optional<String> filter = Optional.empty();

	@Override
	protected Page<Product> fetchFromBackEnd(Query<Product, Object> query, Pageable pageable) {
		return getProductService().findAnyMatching(filter, pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<Product, Object> query) {
		return (int) getProductService().countAnyMatching(filter);
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
	public Object getId(Product item) {
		return item.getId();
	}

	protected ProductService getProductService() {
		return service = BeanLocator.use(service).orElseFindInstance(ProductService.class);
	}
}